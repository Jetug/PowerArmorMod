package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.common.foundation.item.DrillItem;
import com.jetug.power_armor_mod.common.foundation.particles.Pos3D;
import com.jetug.power_armor_mod.common.foundation.container.menu.ArmorStationMenu;
import com.jetug.power_armor_mod.common.foundation.container.menu.PowerArmorMenu;
import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.data.enums.DashDirection;
import com.jetug.power_armor_mod.common.foundation.item.ChassisEquipment;
import com.jetug.power_armor_mod.common.foundation.item.ChassisArmor;
import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.foundation.registery.ItemRegistry;
import com.jetug.power_armor_mod.common.util.helpers.*;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
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
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.ParticleKeyFrameEvent;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static com.jetug.generated.animations.ArmorChassisAnimation.*;
import static com.jetug.power_armor_mod.common.foundation.EntityHelper.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.util.helpers.AnimationHelper.*;
import static com.jetug.power_armor_mod.common.util.helpers.MathHelper.*;
import static com.jetug.power_armor_mod.common.util.helpers.timer.PovUtils.*;
import static net.minecraft.client.renderer.debug.DebugRenderer.*;
import static net.minecraft.util.Mth.*;
import static net.minecraft.world.InteractionHand.*;
import static org.apache.logging.log4j.Level.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;

public class WearableChassis extends ArmorChassisBase implements IAnimatable {
    public static final float ROTATION = (float) Math.PI / 180F;
    public static final int EFFECT_DURATION = 9;
    public static final int MAX_PUNCH_FORCE = 20;
    public static final int DASH_DURATION = 10;
    public static final int PUNCH_DURATION = 10;

    public final Speedometer speedometer = new Speedometer(this);

    protected boolean isJumping;
    protected float playerJumpScale;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private boolean isDashing = false;
    private boolean isPunching = false;
    private DashDirection dashDirection;

    public WearableChassis(EntityType<? extends ArmorChassisBase> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return ArmorChassisBase.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1000.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.0D)
                .add(Attributes.JUMP_STRENGTH, 0.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8D);
    }

//    @Override
//    public EntityDimensions getDimensions(Pose pPose) {
//        return super.getDimensions(pPose).scale(1,  isShiftDown()? 0.5f : 1);

//    }


    @Override
    public void tick() {
        super.tick();

        if(isServerSide && isDashing && dashDirection != DashDirection.UP) {
            pushEntitiesAround();
        }

        applyEffects();
        speedometer.tick();
        timer.tick();
    }

    @Override
    public void addAttackCharge() {
        if (hasPlayerPassenger() && hasPowerKnuckle() && (playerHandIsEmpty() || isDrillItemInHand()))
            super.addAttackCharge();
    }

    public boolean hasFireProtection(){
        return getEquipment(BODY_FRAME).getItem() == ItemRegistry.FIREPROOF_COATING.get();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float damage) {
        Global.LOGGER.log(INFO, "HURT isClientSide: " + level.isClientSide);

        float finalDamage = getDamageAfterAbsorb(damage);

        if(damageSource == DamageSource.CACTUS)
            return false;

        if(isServerSide){
            damageArmorItem(HELMET, damageSource , damage);
            damageArmorItem(BODY_ARMOR, damageSource , damage);
            damageArmorItem(LEFT_ARM_ARMOR, damageSource , damage);
            damageArmorItem(RIGHT_ARM_ARMOR, damageSource , damage);

            if(damageSource == DamageSource.FALL) {
                damageArmorItem(LEFT_LEG_ARMOR, damageSource, damage);
                damageArmorItem(RIGHT_LEG_ARMOR, damageSource, damage);
            }
        }

        if (hasPlayerPassenger() && !(damageSource.isFire() && hasFireProtection())) {
            getPlayerPassenger().hurt(damageSource, finalDamage);
        }

        return true;
    }

    @Override
    public boolean causeFallDamage(float height, float multiplier, DamageSource damageSource) {
        //if (height > 1) this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        if (height > 4 && hasPlayerPassenger() && !getPlayerPassenger().isShiftKeyDown())
            pushEntitiesAround();

        int damage = this.calculateFallDamage(height, multiplier);
        if (damage <= 0)
            return false;

        if(height >= 20 )
            this.hurt(damageSource, damage);

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
        var stack = player.getItemInHand(hand);

        if(isServerSide && !player.isPassenger()) {
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

        var yOffset = getPlayerPassenger().isShiftKeyDown() ?  1.2f : 1.0f;
        var posY = getY() + getPassengersRidingOffset() + entity.getMyRidingOffset() - yOffset;
        entity.setPos(getX(), posY, getZ());

        if (entity instanceof LivingEntity livingEntity)
            livingEntity.yBodyRot = yBodyRot;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (!isAlive()) return;
        if (isVehicle() && hasPlayerPassenger())
            travelWithPlayer(travelVector);
        else {
            this.flyingSpeed = 0.02F;
            super.travel(travelVector);
        }
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

    public void damageArmorItem(BodyPart bodyPart, DamageSource damageSource, float damage) {
        Global.LOGGER.info("damageArmorItem" + isClientSide);
        var itemStack = inventory.getItem(bodyPart.getId());

        if(itemStack.getItem() instanceof ChassisArmor armorItem)
            armorItem.damageArmor(itemStack, (int) damage);
    }

    @Nullable
    public ChassisEquipment getEquipmentItem(BodyPart part) {
        var stack = getEquipment(part);
        if(!stack.isEmpty())
            return (ChassisEquipment) stack.getItem();
        return null;
    }

    public void openGUI(Player player) {
        Global.referenceMob = this;

        if (isServerSide) {
            player.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory menu, Player player) {
                    return new PowerArmorMenu(id, inventory, menu, WearableChassis.this);
                }

                @Override
                public Component getDisplayName() {
                    return WearableChassis.this.getDisplayName();
                }
            });
        }
    }

    public void openStationGUI(Player player) {
        Global.referenceMob = this;

        if (isServerSide) {
            player.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory menu, Player player) {
                    return new ArmorStationMenu(id, inventory, menu, WearableChassis.this);
                }

                @Override
                public Component getDisplayName() {
                    return WearableChassis.this.getDisplayName();
                }
            });
        }
    }

    public boolean isPunching() {
        return isPunching;
    }

    public boolean isDashing() {
        return isDashing;
    }

    public Boolean isWalking(){
        if (!hasPlayerPassenger())
            return false;

        var player = getPlayerPassenger();
        return player.xxa != 0.0 || player.zza != 0.0;
    }

    public boolean hasPlayerPassenger(){
        return getControllingPassenger() instanceof Player;
    }

    public Player getPlayerPassenger(){
        if(getControllingPassenger() instanceof Player player)
            return player;
        return null;
    }

    public ItemStack getPlayerItem(EquipmentSlot slot){
        return hasPlayerPassenger() ? getPlayerPassenger().getItemBySlot(slot) : ItemStack.EMPTY;
    }

    public void jump(){
        playerJumpScale = 1.0F;
    }

    private int getDashHeat(){
        return hasJetpack() ? getJetpack().heat : 0;
    }

    public void dash(DashDirection direction) {
        if (!hasJetpack() || !hasEquipment(ENGINE)) return;

        heatController.doHeatAction(getDashHeat(), () -> applyToPlayer((player) -> {
            dashDirection = direction;
            isDashing = true;
            timer.addCooldownTimer(DASH_DURATION, () -> isDashing = false);
                dashInDirection(direction, getJetpack().force);
        }));
    }

    public void powerPunch(){
        if(!hasPowerKnuckle()) return;

        powerPunch(() -> {
            if(!getPlayerPassenger().isShiftKeyDown())
                dash(DashDirection.FORWARD);
            var optional = getTargetedEntity(getPlayerPassenger(), 5);
            if(optional.isPresent() && optional.get() instanceof LivingEntity livingEntity){
                powerPunchTarget(livingEntity);
            }
            else{
                var pick = getBlockHitResult(level, getPlayerPassenger(), ClipContext.Fluid.NONE);
                if(pick.getType() == HitResult.Type.BLOCK) powerPunchBlock(pick.getBlockPos());
            }
        });
    }

    private void powerPunchTarget(LivingEntity target){
        var vector = getPlayerPassenger().getViewVector(1.0F);
        var force = getPunchForce();

        //target.knockback(force, vector.x, vector.z);
        EntityUtils.push(target, vector.multiply(force, force, force));
    }

    private void powerPunchBlock(BlockPos blockPos){
        var force = getPunchForce();
        var player = getPlayerPassenger();

        if(force == MAX_PUNCH_FORCE)
            dig(blockPos, player);
    }


    private void dig(BlockPos pos, Player player){
        var direction = player.getDirection();
        var centerPos = pos.offset(direction.getNormal());

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                BlockPos targetPos = centerPos.offset(direction.getStepX() * xOffset, yOffset, direction.getStepZ() * xOffset);
                player.level.destroyBlock(targetPos, true);
            }
        }
    }

    private void powerPunch(Runnable runnable){
        if(!hasPlayerPassenger() || attackCharge == 0) return;

        heatController.doHeatAction(getPowerKnuckle().heat, () -> {
            isPunching = true;
            timer.addCooldownTimer(PUNCH_DURATION, () -> isPunching = false);
            runnable.run();
        });

        resetAttackCharge();
    }

    private float getPunchForce(){
        var charge = getAttackChargeInPercent();
        return getPercentOf(MAX_PUNCH_FORCE, charge);
    }

    private void applyToPlayer(Consumer<Player> consumer){
        if(!hasPlayerPassenger()) return;
        consumer.accept(getPlayerPassenger());
    }

    private void dashInDirection(DashDirection direction, float force) {
        if(!hasPlayerPassenger()) return;
        float viewYRot = getPlayerPassenger().getViewYRot(1);
        float x = sin(viewYRot * ROTATION) * force;
        float z = cos(viewYRot * ROTATION) * force;
        var vector = Vec3.ZERO;

        switch (direction) {
            case FORWARD -> vector = new Vec3(-x, 0, z);
            case BACK    -> vector = new Vec3(x, 0, -z);
            case RIGHT   -> vector = rotateVector(viewYRot, 90, force);
            case LEFT    -> vector = rotateVector(viewYRot, -90, force);
            case UP      -> vector = new Vec3(0, force / 2, 0);
        }

        setDeltaMovement(getDeltaMovement().add(vector));
    }

    private Vec3 rotateVector(float viewYRot, int angle, float force){
        float rot = (viewYRot + angle) * ROTATION;
        return new Vec3(-sin(rot) * force, 0, cos(rot) * force);
    }

    private float getDamageAfterAbsorb(float damage){
        return CombatRules.getDamageAfterAbsorb(damage, totalDefense,totalToughness);
    }

    private void doPlayerRide(Player player) {
        player.setYRot(getYRot());
        player.setXRot(getXRot());
        player.startRiding(this);
    }

    private boolean isDrillItemInHand() {
        return getPlayerPassenger().getItemInHand(MAIN_HAND).getItem() instanceof DrillItem;
    }

    private boolean playerHandIsEmpty() {
        return getPlayerPassenger().getItemInHand(MAIN_HAND).isEmpty();
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
            if(hasFireProtection()) {
                addEffect(player, MobEffects.FIRE_RESISTANCE, 1);
                player.clearFire();
            }
        }
    }

    private void addEffect(Player player, MobEffect effect, int amplifier){
        player.addEffect(new MobEffectInstance(effect, WearableChassis.EFFECT_DURATION, amplifier, false, false));
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
        for(LivingEntity entity : getEntitiesInRange(2, 1,2)) {
            if (entity == this || entity == getControllingPassenger())
                continue;
            //var push = 0.5;
            var direction = VectorHelper.getDirection(position(), entity.position());
            entity.knockback(1, direction.x, direction.z);
            //entity.push(direction.x * push, 0.5, direction.z * push);
            entity.hurt(DamageSource.ANVIL, 10);
        }
    }

    private List<LivingEntity> getEntitiesInRange(double x, double y, double z) {
        return this.level.getEntitiesOfClass(LivingEntity.class, new AABB(position(), position()).inflate(x, y, z));
    }

    @Override
    public void registerControllers(AnimationData data) {
        var armsController = new AnimationController<>(this, "arm_controller", 0, this::animateArms);
        armsController.registerSoundListener(this::soundListener);
        armsController.registerParticleListener(this::particleListener);
        data.addAnimationController(armsController);
        data.addAnimationController(new AnimationController<>(this, "leg_controller", 0, this::animateLegs));
    }

    private <E extends IAnimatable> PlayState animateArms(AnimationEvent<E> event) {
        var controller = event.getController();
        controller.animationSpeed = 1.0D;

        var player = getPlayerPassenger();

        if (player != null) {
            if (isDashing) {
                return animateDash(controller);
            }
            else if(isPunching){
                controller.animationSpeed = 2;
                setAnimation(controller, POWER_PUNCH, HOLD_ON_LAST_FRAME);
                return PlayState.CONTINUE;
            }
            else if(isMaxCharge()){
                setAnimation(controller, POWER_PUNCH_CHARGE_LOOP, LOOP);
                return PlayState.CONTINUE;
            }
            else if(isChargingAttack()){
                setAnimation(controller, POWER_PUNCH_CHARGE, PLAY_ONCE);
                controller.animationSpeed = 0.7;
                return PlayState.CONTINUE;
            }
            else if (player.attackAnim > 0) {
                controller.animationSpeed = 2.0D;
                setAnimation(controller, HIT, PLAY_ONCE);
                return PlayState.CONTINUE;
            } else if (hurtTime > 0) {
                setAnimation(controller, HURT, PLAY_ONCE);
                return PlayState.CONTINUE;
            }
            else if (isWalking()) {
                setAnimation(controller, WALK_ARMS, LOOP);
                controller.animationSpeed = speedometer.getSpeed() * 4.0D;
                return PlayState.CONTINUE;
            }
        }

        setAnimation(controller, "idle", LOOP);
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState animateLegs(AnimationEvent<E> event) {
        var controller = event.getController();
        controller.animationSpeed = 1.0D;

        if (!hasPlayerPassenger()) return PlayState.STOP;
        var player = getPlayerPassenger();

        if(!isDashing) {
            if (this.isWalking()) {
                if (player.isShiftKeyDown()){
                    setAnimation(controller, SNEAK_WALK, LOOP);
                }
                else {
                    setAnimation(controller, WALK_LEGS, LOOP);
                    controller.animationSpeed = speedometer.getSpeed() * 4.0D;
                }
                return PlayState.CONTINUE;
            }
            else if (player.isShiftKeyDown()) {
                setAnimation(controller, SNEAK_END, LOOP);
                return PlayState.CONTINUE;
            }
        }
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState animateDash(AnimationController<E> controller) {
        switch (dashDirection) {
            case FORWARD -> setAnimation(controller, DASH_FORWARD, HOLD_ON_LAST_FRAME);
            case BACK   -> setAnimation(controller, DASH_BACK, HOLD_ON_LAST_FRAME);
            case RIGHT -> setAnimation(controller, DASH_RIGHT, HOLD_ON_LAST_FRAME);
            case LEFT -> setAnimation(controller, DASH_LEFT, HOLD_ON_LAST_FRAME);
            case UP  -> setAnimation(controller, DASH_UP, HOLD_ON_LAST_FRAME);
        }
        return PlayState.CONTINUE;
    }

    private <ENTITY extends IAnimatable> void particleListener(ParticleKeyFrameEvent<ENTITY> event) {

//        HashMap<String, BoneAnimationQueue> map = event.getController().getBoneAnimationQueues();
//
//        var bone = (GeoBone)map.get("left_jet2_frame").bone();// armorChassisModel.getAnimationProcessor().getBone("left_jet2_frame");
//        showJetpackParticles(this, bone);
    }

    private <ENTITY extends IAnimatable> void soundListener(SoundKeyframeEvent<ENTITY> event) {
        var t = event.sound;
        t.getBytes();
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {

            this.playSound(SoundEvents.BELL_BLOCK, 0.4F, 1.0F);

//
//            player.playSound(SoundRegistry.JACK_MUSIC.get(), 1, 1);
        }
    }

    private void travelWithPlayer(Vec3 travelVector) {
        var player = getPlayerPassenger();
        setRotationMatchingPassenger(player);

        if (playerJumpScale > 0.0F && !isJumping() && onGround)
            makeJump(player);

        this.flyingSpeed = getSpeed() * 0.1F;

        if (isControlledByLocalInstance())
            super.travel(new Vec3(player.xxa, travelVector.y, player.zza));
        else setDeltaMovement(Vec3.ZERO);

        if (onGround) {
            playerJumpScale = 0.0F;
            isJumping = false;
        }
    }

    private void makeJump(Player player) {
        var jump = getCustomJump() * playerJumpScale * getBlockJumpFactor();
        setDeltaMovement(getDeltaMovement().x, jump, getDeltaMovement().z);
        isJumping  = true;
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