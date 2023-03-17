package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.client.gui.PowerArmorContainer;
import com.jetug.power_armor_mod.common.foundation.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.jetug.power_armor_mod.common.util.helpers.*;
import com.jetug.power_armor_mod.common.util.helpers.timer.*;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import javax.annotation.Nullable;
import java.util.List;

import static com.jetug.power_armor_mod.common.foundation.EntityHelper.giveEntityItemToPlayer;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.util.helpers.MathHelper.getPercentOf;
import static net.minecraft.util.Mth.*;
import static org.apache.logging.log4j.Level.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;

public class PowerArmorEntity extends PowerArmorBase implements IAnimatable {
    public static final float ROTATION = (float) Math.PI / 180F;
    public static final int EFFECT_DURATION = 9;
    public static final int DASH_HEAT = 100;
    public static final int MAX_PUNCH_FORCE = 20;
    public static final int DASH_DURATION = 10;

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
    public final Speedometer speedometer = new Speedometer(this);

    protected boolean isJumping;
    protected float playerJumpScale;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private boolean isDashing = false;
    private DashDirection dashDirection;

    public PowerArmorEntity(EntityType<? extends LivingEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.JUMP_STRENGTH, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

    @Override
    public void tick() {
        super.tick();

        if(isServerSide && isDashing) {
            pushEntitiesAround();
        }

        syncDataWithClient();
        applyEffects();

        speedometer.tick();
        timer.tick();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        Global.LOGGER.log(INFO, "HURT isClientSide: " + level.isClientSide);

        float finalDamage = getDamageAfterAbsorb(damage);

        if (hasPlayerPassenger())
            getPlayerPassenger().hurt(damageSource, finalDamage);

        if(isServerSide){
            damageArmorItem(HEAD     ,damageSource , damage);
            damageArmorItem(BODY     ,damageSource , damage);
            damageArmorItem(LEFT_ARM ,damageSource , damage);
            damageArmorItem(RIGHT_ARM,damageSource , damage);

            if(damageSource == DamageSource.FALL) {
                damageArmorItem(LEFT_LEG, damageSource, damage);
                damageArmorItem(RIGHT_LEG, damageSource, damage);
            }
        }

        return true;
    }

    @Override
    public boolean causeFallDamage(float height, float multiplier, DamageSource damageSource) {
        if (height > 1) this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        if(height > 4) pushEntitiesAround();

        int damage = this.calculateFallDamage(height, multiplier);
        if (damage <= 0)
            return false;



        this.hurt(damageSource, damage);

//        if (this.isVehicle()) {
//            for(Entity entity : this.getIndirectPassengers()) {
//                entity.hurt(damageSource, (float) damage);
//            }
//        }

        this.playBlockFallSound();
        return true;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(hasPlayerPassenger()) this.yHeadRot = this.getYRot();
    }

    @Override
    public boolean isInvisible() {
        var clientPlayer = Minecraft.getInstance().player;
        var pov = Minecraft.getInstance().options.getCameraType();

        if (hasPassenger(clientPlayer) && pov == CameraType.FIRST_PERSON)
            return true;
        return super.isInvisible();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if (super.isInvulnerableTo(damageSource))
            return true;
        else
            return damageSource.getEntity() == getControllingPassenger();
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vector, InteractionHand hand) {
        Global.LOGGER.log(INFO, level.isClientSide);
        ItemStack stack = player.getItemInHand(hand);

        if(isServerSide) {
            if (stack.getItem() == Items.STICK)
                return giveEntityItemToPlayer(player, this, hand);
            if (player.isShiftKeyDown()) {
                openGUI(player);
                return InteractionResult.SUCCESS;
            } else if (!isVehicle()) {
                this.doPlayerRide(player);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public void positionRider(Entity entity) {
        super.positionRider(entity);

        float posX = sin(this.yBodyRot * ROTATION);
        float posZ = cos(this.yBodyRot * ROTATION);
        double posXZ = -0.3;
        double posY = 0.9;
        entity.setPos(this.getX() + (posXZ * posX),
                this.getY() + this.getPassengersRidingOffset() + entity.getMyRidingOffset() - posY,
                this.getZ() - (posXZ * posZ));

        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        baseTravel(travelVector);
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity p_20123_) {
        return super.getDismountLocationForPassenger(p_20123_);
    }

    @Override
    public void checkDespawn() {}

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    protected float tickHeadTurn(float pYRot, float pAnimStep) {
        if(hasPlayerPassenger())
            return super.tickHeadTurn(pYRot, pAnimStep);
        return pAnimStep;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<PowerArmorEntity> armController = new AnimationController<>(this, "arm_controller", 0, this::animateArms);
        AnimationController<PowerArmorEntity> legController = new AnimationController<>(this, "leg_controller", 0, this::animateLegs);
        data.addAnimationController(armController);
        data.addAnimationController(legController);
    }

    public void damageArmorItem(BodyPart bodyPart, DamageSource damageSource, float damage) {
        Global.LOGGER.info("damageArmorItem" + isClientSide);

        var itemStack = inventory.getItem(bodyPart.getId());

        if(!itemStack.isEmpty()){
            var item = (PowerArmorItem)itemStack.getItem();
            item.damageArmor(itemStack, (int) damage);
        }
    }

    public ItemStack getItem(BodyPart part) {
        return inventory.getItem(part.ordinal());
    }

    public void openGUI(Player player) {
        Global.referenceMob = this;

        if (isServerSide) {
            player.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory menu, Player player) {
                    return new PowerArmorContainer(id, inventory, menu, PowerArmorEntity.this);
                }

                @Override
                public Component getDisplayName() {
                    return PowerArmorEntity.this.getDisplayName();
                }
            });
        }
    }

    public boolean hasPlayerPassenger(){
        return getControllingPassenger() instanceof Player;
    }

    @Nullable
    public Player getPlayerPassenger(){
        if(getControllingPassenger() instanceof Player player)
            return player;
        return null;
    }

    public void jump(){
        playerJumpScale = 1.0F;
    }

    public void dash(DashDirection direction) {
        doHeatAction(DASH_HEAT, () -> _dash(direction));
    }

    public void push(Vec3 vector){
        setDeltaMovement(getDeltaMovement().add(vector));
    }

    public void punchTarget(Entity target){
        if(!hasPlayerPassenger()) return;

        var vector = getPlayerPassenger().getViewVector(1.0F);
        var force = getPercentOf(MAX_PUNCH_FORCE, getAttackChargeInPercent());

        target.push(vector.x * force, vector.y * force, vector.z * force);
    }

    public void addAttackCharge(int attackCharge) {
        if(this.attackCharge + attackCharge <= MAX_ATTACK_CHARGE)
            this.attackCharge += attackCharge;
    }

    public void resetAttackCharge() {
        this.attackCharge = 0;
    }

    private void _dash(DashDirection direction){
        if (!(getControllingPassenger() instanceof Player player))
            return;

        isDashing = true;
        dashDirection = direction;
        timer.addTimer(new PlayOnceTimerTask(DASH_DURATION, () -> isDashing = false));

        push(direction, player);
    }

    private void push(DashDirection direction, Player player) {
        float viewYRot = player.getViewYRot(1);
        float x = sin(viewYRot * ROTATION) * 3;
        float z = cos(viewYRot * ROTATION) * 3;
        var vector = Vec3.ZERO;

        switch (direction) {
            case FORWARD -> vector = new Vec3(-x, 0, z);
            case BACK    -> vector = new Vec3(x, 0, -z);
            case RIGHT   -> vector = rotateVector(viewYRot, 90);
            case LEFT    -> vector = rotateVector(viewYRot, -90);
            case UP      -> vector = new Vec3(0, 1, 0);
        }

        push(vector);
    }

    private Vec3 rotateVector(float viewYRot, int angle){
        float rot = (viewYRot + angle) * ROTATION;
        return new Vec3(-sin(rot), 0, cos(rot));
    }

    private float getDamageAfterAbsorb(float damage){
        return CombatRules.getDamageAfterAbsorb(damage, totalDefense,totalToughness);
    }

    private void doPlayerRide(Player player) {
        player.setYRot(getYRot());
        player.setXRot(getXRot());
        player.startRiding(this);
    }

    private boolean isJumping() {
        return this.isJumping;
    }

    private double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }

    private void applyEffects(){
        var player = getPlayerPassenger();

        if(player != null) {
            addEffect(player, MobEffects.DIG_SPEED         , 1);
            addEffect(player, MobEffects.DAMAGE_BOOST      , 1);
            addEffect(player, MobEffects.DAMAGE_RESISTANCE , 1);
        }
    }

    private void addEffect(Player player, MobEffect effect, int amplifier){
        player.addEffect(new MobEffectInstance(effect, PowerArmorEntity.EFFECT_DURATION, amplifier, false, false));
    }

//    @Override
//    public boolean causeFallDamage(float height, float p_225503_2_, @NotNull DamageSource damageSource) {
//        pushEntitiesAround();
//
//        int immune = 5;
//        int damage = this.calculateFallDamage(height, p_225503_2_) / 2;
//        if (damage <= immune) {
//            return false;
//        } else {
//            if (this.isVehicle()) {
//                for(Entity entity : getIndirectPassengers()) {
//                    entity.hurt(DamageSource.FALL, damage - immune);
//                }
//            }
//            animateHurt();
//            playBlockFallSound();
//            return true;
//        }
//    }

    private void pushEntitiesAround(){
        for(Entity entity : getEntitiesOfClass(3, 1,3)) {
            if (entity == this || entity == getControllingPassenger())
                continue;
            double push = 0.5;
            Vec3 direction = VectorHelper.getDirection(position(), entity.position());
            entity.push(direction.x * push, 0.5, direction.z * push);
            entity.hurt(DamageSource.ANVIL, 10);
        }
    }

    private List<Entity> getEntitiesOfClass(double x, double y, double z) {
        return this.level.getEntitiesOfClass(Entity.class, new AABB(position(), position()).inflate(x, y, z));
    }

    private <E extends IAnimatable> PlayState animateArms(AnimationEvent<E> event) {
        AnimationController<E> controller = event.getController();
        controller.animationSpeed = 1.0D;

        var player = getPlayerPassenger();
        if (player != null) {
            if (player.attackAnim > 0) {
                controller.animationSpeed = 2.0D;
                setAnimation(controller, "hit", PLAY_ONCE);
                return PlayState.CONTINUE;
            } else if (isDashing) {
                return animateDash(controller);
            } else if (hurtTime > 0) {
                setAnimation(controller, "hurt", PLAY_ONCE);
                return PlayState.CONTINUE;
            }
            else if (event.isMoving()) {
                setAnimation(controller, "walk_arms", LOOP);
                controller.animationSpeed = speedometer.getSpeed() * 4.0D;
                return PlayState.CONTINUE;
            }
        }

        setAnimation(controller, "idle", LOOP);
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState animateLegs(AnimationEvent<E> event) {
        AnimationController<E> controller = event.getController();
        controller.animationSpeed = 1.0D;

        if (getPlayerPassenger() == null) return PlayState.STOP;

        if (event.isMoving()) {
            setAnimation(controller, "walk_legs", LOOP);
            controller.animationSpeed = speedometer.getSpeed() * 4.0D;
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState animateDash(AnimationController<E> controller) {
        switch (dashDirection) {
            case FORWARD -> setAnimation(controller, "dash_forward", HOLD_ON_LAST_FRAME);
            case BACK   -> setAnimation(controller, "dash_back", HOLD_ON_LAST_FRAME);
            case RIGHT -> setAnimation(controller, "dash_right", HOLD_ON_LAST_FRAME);
            case LEFT -> setAnimation(controller, "dash_left", HOLD_ON_LAST_FRAME);
            case UP  -> setAnimation(controller, "dash_back", HOLD_ON_LAST_FRAME);
        }
        return PlayState.CONTINUE;
    }

    private void setAnimation(AnimationController<?> controller, String name, ILoopType loopType){
        controller.setAnimation(new AnimationBuilder().addAnimation(name, loopType));
    }

    private void baseTravel(Vec3 travelVector) {
        if (!isAlive()) return;
        if (isVehicle() && hasPlayerPassenger())
            travelWithPlayerPassenger(travelVector);
        else {
            this.flyingSpeed = 0.02F;
            super.travel(travelVector);
        }
    }

    private void travelWithPlayerPassenger(Vec3 travelVector) {
        var player = getPlayerPassenger();
        setRotationMatchingPassenger(player);

        if (playerJumpScale > 0.0F && !isJumping() && onGround)
            makeJump(player);

        this.flyingSpeed = getSpeed() * 0.1F;

        if (isControlledByLocalInstance()) {
            this.setSpeed((float)getAttributeValue(Attributes.MOVEMENT_SPEED));
            super.travel(new Vec3(player.xxa, travelVector.y, player.zza));
        } else {
            setDeltaMovement(Vec3.ZERO);
        }

        if (onGround) {
            playerJumpScale = 0.0F;
            isJumping = false;
        }
    }

    private void makeJump(Player player) {
        var jump = getCustomJump() * playerJumpScale * getBlockJumpFactor();
        setDeltaMovement(getDeltaMovement().x, jump, getDeltaMovement().z);
        isJumping = true;
        hasImpulse = true;

        if (player.zza > 0.0F) {
            float x = sin(getYRot() * ROTATION);
            float z = cos(getYRot() * ROTATION);
            setDeltaMovement(getDeltaMovement().add(
                    -0.4F * x * playerJumpScale,
                    0.0D,
                    0.4F * z * playerJumpScale));
        }

        playerJumpScale = 0.0F;
    }

    private void setRotationMatchingPassenger(LivingEntity livingEntity) {
        this.yRotO = getYRot();
        this.setYRot(livingEntity.getYRot());
        this.setXRot(livingEntity.getXRot() * 0.5F);
        this.setRot(getYRot(), getXRot());
    }
}