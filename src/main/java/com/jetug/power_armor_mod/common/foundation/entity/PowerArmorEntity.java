package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.client.gui.PowerArmorContainer;
import com.jetug.power_armor_mod.common.foundation.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.jetug.power_armor_mod.common.util.helpers.*;
import com.jetug.power_armor_mod.common.util.helpers.timer.*;
import com.jetug.power_armor_mod.common.util.interfaces.SimpleAction;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
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
import static net.minecraft.util.Mth.*;
import static org.apache.logging.log4j.Level.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;

public class PowerArmorEntity extends PowerArmorBase implements IAnimatable {
    public static final float ROTATION = (float) Math.PI / 180F;
    public static final int EFFECT_DURATION = 9;
    public static final String HEAT = "Heat";
    public static final int DASH_HEAT = 100;

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
    public final Speedometer speedometer = new Speedometer(this);

    protected boolean isJumping;
    protected float playerJumpPendingScale;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final TickTimer timer = new TickTimer();

    private boolean isDashing = false;
    private DashDirection dashDirection;

    private int maxHeat = 1000;
    private int heat = 50;

    public void addHeat(int value){
        if(value + heat <= maxHeat)
            heat += value;
        else heat = maxHeat;
    }

    public int getHeat(){
        return heat;
    }

    public int getHeatInPercent(){
        float dd = (float)heat / maxHeat * 100;
        int i = (int)dd ;
        return i;
    }

    public PowerArmorEntity(EntityType<? extends LivingEntity> type, Level worldIn) {
        super(type, worldIn);
        timer.addTimer(new LoopTimerTask(() -> heat -= 1));
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

    public ItemStack getItem(BodyPart part) {
        return inventory.getItem(part.ordinal());
    }

    @Override
    public void tick() {
        super.tick();

        if(!level.isClientSide) {
            if(isDashing) {
                pushEntitiesAround();
            }
        }

        syncDataWithClient();
        applyEffects();

        speedometer.tick();
        timer.tick();
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt(HEAT, heat);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        heat = compound.getInt(HEAT);

    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        Global.LOGGER.log(INFO, "HURT isClientSide: " + level.isClientSide);

        if(isServerSide){
            damageArmorItem(HEAD     , damage, damageSource);
            damageArmorItem(BODY     , damage, damageSource);
            damageArmorItem(LEFT_ARM , damage, damageSource);
            damageArmorItem(RIGHT_ARM, damage, damageSource);
            damageArmorItem(LEFT_LEG , damage, damageSource);
            damageArmorItem(RIGHT_LEG, damage, damageSource);
        }

        return true;
    }

    public void damageArmorItem(BodyPart bodyPart, float damage, DamageSource damageSource) {
        Global.LOGGER.info("damageArmorItem" + isClientSide);

        var itemStack = inventory.getItem(bodyPart.getId());

        if(!itemStack.isEmpty()){
            var item = (PowerArmorItem)itemStack.getItem();
            var trueDamage = ((PowerArmorItem)itemStack.getItem()).getDamageAfterAbsorb(damage);
            item.damageArmor(itemStack, (int)trueDamage);
        }
    }

    public float getPlayerDamageValue(DamageSource damageSource, float damage){
        var itemStack = inventory.getItem(BODY.ordinal());
        if(!itemStack.isEmpty()) {
            return ((PowerArmorItem) itemStack.getItem()).getDamageAfterAbsorb(damage);
        }
        return damage;
    }

    public void hurtPart(BodyPart bodyPart, DamageSource damageSource, float damage) {

    }

    @Override
    public boolean causeFallDamage(float height, float multiplier, DamageSource damageSource) {
        if (height > 1) this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        if(height > 4) pushEntitiesAround();

        int damage = this.calculateFallDamage(height, multiplier);
        if (damage <= 0) {
            return false;
        } else {
            this.hurt(damageSource, damage);
            if (this.isVehicle()) {
                for(Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(damageSource, (float) damage);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(hasPlayer()) this.yHeadRot = this.getYRot();
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
        if (!this.isAlive()) return;

        if (this.isVehicle() &&  getControllingPassenger() instanceof Player) {
            var livingEntity = (LivingEntity)this.getControllingPassenger();
            assert livingEntity != null;
            this.setYRot(livingEntity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(livingEntity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());

            if (playerJumpPendingScale > 0.0F && !this.isJumping() && this.onGround) {
                double jump = this.getCustomJump() * playerJumpPendingScale * this.getBlockJumpFactor();

                Vec3 deltaMovement = this.getDeltaMovement();
                this.setDeltaMovement(deltaMovement.x, jump, deltaMovement.z);
                isJumping = true;
                hasImpulse = true;

                if (livingEntity.zza > 0.0F) {
                    float f2 = sin(this.getYRot() * ROTATION);
                    float f3 = cos(this.getYRot() * ROTATION);
                    this.setDeltaMovement(this.getDeltaMovement().add(
                            -0.4F * f2 * playerJumpPendingScale,
                            0.0D,
                            0.4F * f3 * playerJumpPendingScale));
                }

                playerJumpPendingScale = 0.0F;
            }

            this.flyingSpeed = this.getSpeed() * 0.1F;

            if (this.isControlledByLocalInstance()) {
                this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                super.travel(new Vec3(livingEntity.xxa, travelVector.y, livingEntity.zza));
            } else if (livingEntity instanceof Player) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            if (this.onGround) {
                playerJumpPendingScale = 0.0F;
                isJumping = false;
            }
        }
        else {
            this.flyingSpeed = 0.02F;
            super.travel(travelVector);
        }
    }

    @Override
    protected float tickHeadTurn(float pYRot, float pAnimStep) {
        if(hasPlayer())
            return super.tickHeadTurn(pYRot, pAnimStep);
        return pAnimStep;
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

    public void doPlayerRide(Player player) {
        player.setYRot(getYRot());
        player.setXRot(getXRot());
        player.startRiding(this);
    }

    public boolean hasPlayer(){
        return getControllingPassenger() instanceof Player;
    }

    public void jump(){
        playerJumpPendingScale = 1.0F;
    }

    public void dash(DashDirection direction) {
        doHeatAction(DASH_HEAT, () -> _dash(direction));
    }

    public void push(Vec3 vector){
        setDeltaMovement(getDeltaMovement().add(vector));
    }

    private void _dash(DashDirection direction){
        if (!(getControllingPassenger() instanceof Player player))
            return;

        isDashing = true;
        dashDirection = direction;
        timer.addTimer(new PlayOnceTimerTask(10, () -> isDashing = false));

        float viewYRot = player.getViewYRot(1);
        float rotation = viewYRot * ROTATION;
        float x = sin(rotation) * 3;
        float z = cos(rotation) * 3;

        var vector = new Vec3(-x, 0, z);

        switch (direction) {
            case FORWARD -> vector = new Vec3(-x, 0, z);
            case BACK -> vector = new Vec3(x, 0, -z);
            case RIGHT -> {
                float rot = (viewYRot + 90) * ROTATION;
                vector = new Vec3(-sin(rot), 0, cos(rot));
            }
            case LEFT -> {
                float rot = (viewYRot - 90) * ROTATION;
                vector = new Vec3(-sin(rot), 0, cos(rot));
            }
            case UP -> vector = new Vec3(0, 1, 0);
        }

        push(vector);
    }

    private void doHeatAction(int heat, SimpleAction action){
        if(!canDoAction(heat)) return;
        action.execute();
        addHeat(heat);
    }

    private boolean canDoAction(int heat){
        return heat + this.heat <= maxHeat;
    }

//    public boolean hurt(PowerArmorPartEntity part, DamageSource damageSource, float damage) {
//        damageArmorItem(part.bodyPart, damage);
//        return super.hurt(damageSource, damage);
//    }



    private boolean isJumping() {
        return this.isJumping;
    }


    private double getCustomJump() {
        return this.getAttributeValue(Attributes.JUMP_STRENGTH);
    }

    private void applyEffects(){
        var player = getPlayer();

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

    @Nullable
    private Player getPlayer(){
        if(getControllingPassenger() instanceof Player player)
            return player;
        return null;
    }

    private <E extends IAnimatable> PlayState animateArms(AnimationEvent<E> event) {
        AnimationController<E> controller = event.getController();
        controller.animationSpeed = 1.0D;

        var player = getPlayer();
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

        var player = getPlayer();
        if (player != null) {
            if (event.isMoving()) {
                setAnimation(controller, "walk_legs", LOOP);
                controller.animationSpeed = speedometer.getSpeed() * 4.0D;
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    @NotNull
    private <E extends IAnimatable> PlayState animateDash(AnimationController<E> controller) {
        switch (dashDirection) {
            case FORWARD -> {
                setAnimation(controller, "dash_forward", HOLD_ON_LAST_FRAME);
            }
            case BACK -> {
                setAnimation(controller, "dash_back", HOLD_ON_LAST_FRAME);
            }
            case RIGHT -> {
                setAnimation(controller, "dash_right", HOLD_ON_LAST_FRAME);
            }
            case LEFT -> {
                setAnimation(controller, "dash_left", HOLD_ON_LAST_FRAME);
            }
            case UP -> {
                setAnimation(controller, "dash_back", HOLD_ON_LAST_FRAME);
            }
        }
        return PlayState.CONTINUE;
    }

    private void setAnimation(AnimationController<?> controller, String name, ILoopType loopType){
        controller.setAnimation(new AnimationBuilder().addAnimation(name, loopType));
    }
}