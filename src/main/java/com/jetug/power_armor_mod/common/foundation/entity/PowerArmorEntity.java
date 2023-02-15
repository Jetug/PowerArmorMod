package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.client.gui.PowerArmorContainer;
import com.jetug.power_armor_mod.common.foundation.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.network.data.ArmorData;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.enums.*;
import com.jetug.power_armor_mod.common.util.helpers.*;
import com.jetug.power_armor_mod.common.util.helpers.timer.*;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
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
import java.util.ArrayList;
import java.util.List;

import static com.jetug.power_armor_mod.common.foundation.registery.ItemsRegistry.*;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;
import static net.minecraft.util.Mth.*;
import static org.apache.logging.log4j.Level.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;

public class PowerArmorEntity extends PowerArmorBase implements IAnimatable {
    public static final float ROTATION = (float) Math.PI / 180F;
    public static final int EFFECT_DURATION = 9;

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
    public final Speedometer speedometer = new Speedometer(this);

    protected boolean isJumping;
    protected float playerJumpPendingScale;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final TickTimer timer = new TickTimer();

    private boolean isDashing = false;
    private DashDirection dashDirection;

    private int maxHeat = 100;
    private int heat = 0;

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
    public boolean hurt(DamageSource damageSource, float damage) {
        Global.LOGGER.log(INFO, "HURT isClientSide: " + level.isClientSide);

        if(!isClientSide){
            damageArmor(HEAD     , damage);
            damageArmor(BODY     , damage);
            damageArmor(LEFT_ARM , damage);
            damageArmor(RIGHT_ARM, damage);
            damageArmor(LEFT_LEG , damage);
            damageArmor(RIGHT_LEG, damage);
        }
       
        var attacker = damageSource.getEntity();
        if(attacker != null) {
            var minecraft = Minecraft.getInstance();
            minecraft.getProfiler().push("pick");
            minecraft.crosshairPickEntity = null;
            double d0 = attacker.getPickRadius();

            Vec3 vec3 = this.getEyePosition();
            double d1 = d0;

            d1 *= d1;
            if (minecraft.hitResult != null) {
                d1 = minecraft.hitResult.getLocation().distanceToSqr(vec3);
            }

            Vec3 vec31 = attacker.getViewVector(1.0F);
            Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
            AABB aabb = attacker.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);

            var entityHitResult = ProjectileUtil.getEntityHitResult(attacker, vec3, vec32, aabb, (p_172770_) ->
                    !p_172770_.isSpectator() && p_172770_.isPickable(), d1);

            //if(entityHitResult != null)
            if (entityHitResult != null) {
                var loc = entityHitResult.getLocation();
                var attackerAye = attacker.getEyePosition();
                var attackerForward = attacker.getForward();
                var attackVector = attackerAye.subtract(this.getEyePosition()).add(attackerForward);
                var headAABB = new AABB(position().add(0, 2.1, 0), position().add(0.6, 2.1 + 0.6, 0.6));
                //var headAABB = new AABB(new Vec3(0, 2.1, 0), new Vec3(0.6, 2.1 + 0.6, 0.6));
                if (headAABB.contains(loc)) {
                    Global.LOGGER.log(INFO, "HURT HEAD");
                }
            }
        }
        return super.hurt(damageSource, damage);
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
    public void aiStep() {
        super.aiStep();

        if(getControllingPassenger() instanceof Player player) {
            this.yHeadRot = this.getYRot();
            //this.yBodyRot = player.yBodyRot;
        }
    }

    @Override
    public void checkDespawn() {}

//    @Override
//    public boolean isPickable() {
//        return true;
//    }

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
        if (super.isInvulnerableTo(damageSource)) {
            return true;
        }
        else return damageSource.getEntity() == getControllingPassenger();
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vector, InteractionHand hand) {
        Global.LOGGER.log(INFO, level.isClientSide);
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() == PA_FRAME.get())
            return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            openGUI(player);
            return InteractionResult.SUCCESS;
        }
        else if(!isVehicle()) {
            this.doPlayerRide(player);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    @Nullable
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
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean canBeRiddenInWater(Entity rider) {
        return true;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity p_20123_) {
        return super.getDismountLocationForPassenger(p_20123_);
    }

    @Override
    public boolean causeFallDamage(float height, float p_149500_, @NotNull DamageSource p_149501_) {
        if (height > 1.0F) {
            this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        }

        int damage = this.calculateFallDamage(height, p_149500_);
        if (damage <= 0) {
            return false;
        } else {
            this.hurt(p_149501_, damage);
            if (this.isVehicle()) {
                for(Entity entity : this.getIndirectPassengers()) {
                    entity.hurt(p_149501_, (float) damage);
                }
            }

            this.playBlockFallSound();
            return true;
        }
    }

    public void openGUI(Player playerEntity) {
        Global.referenceMob = this;

        if (!this.level.isClientSide) {
            playerEntity.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int p_createMenu_1_, Inventory p_createMenu_2_, Player p_createMenu_3_) {
                    return new PowerArmorContainer(p_createMenu_1_, inventory, p_createMenu_2_, PowerArmorEntity.this);
                }

                @Override
                public Component getDisplayName() {
                    return PowerArmorEntity.this.getDisplayName();
                }
            });
        }
    }


    public enum ArmorAction{
        DASH
    }

    private boolean canDoAction(ArmorAction action){
        return false;
    }

    public void dash(DashDirection direction) {
        if (!(getControllingPassenger() instanceof Player player) || !isOnGround())
            return;

        isDashing = true;
        timer.addTimer(new PlayOnceTimerTask(10, () -> isDashing = false));
        dashDirection = direction;

        float viewYRot = player.getViewYRot(1);
        float rotation = viewYRot * ROTATION;
        float x = sin(rotation) * 3;
        float z = cos(rotation) * 3;

        Vec3 vector = new Vec3(-x, 0, z);

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

    public void push(Vec3 vector){
        setDeltaMovement(getDeltaMovement().add(vector));
    }

    public boolean hurt(PowerArmorPartEntity part, DamageSource damageSource, float damage) {
        damageArmor(part.bodyPart, damage);
        return super.hurt(damageSource, damage);
    }

    public boolean isJumping() {
        return this.isJumping;
    }

    public void doPlayerRide(Player player) {
        player.setYRot(getYRot());
        player.setXRot(getXRot());
        player.startRiding(this);
    }

    public boolean hasPlayer(){
        return getControllingPassenger() instanceof Player;
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

    public void pushEntitiesAround(){
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

    public void jump(){
        playerJumpPendingScale = 1.0F;
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