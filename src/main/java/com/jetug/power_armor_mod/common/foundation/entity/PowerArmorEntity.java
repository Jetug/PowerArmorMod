package com.jetug.power_armor_mod.common.foundation.entity;

import com.jetug.power_armor_mod.common.foundation.screen.menu.ArmorStationMenu2;
import com.jetug.power_armor_mod.common.foundation.screen.menu.PowerArmorMenu;
import com.jetug.power_armor_mod.common.data.enums.*;
import com.jetug.power_armor_mod.common.data.enums.DashDirection;
import com.jetug.power_armor_mod.common.foundation.item.EquipmentBase;
import com.jetug.power_armor_mod.common.foundation.item.PowerArmorItem;
import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.util.helpers.*;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import javax.annotation.Nullable;
import java.util.List;

import static com.jetug.power_armor_mod.common.foundation.EntityHelper.*;
import static com.jetug.power_armor_mod.common.data.enums.BodyPart.*;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;
import static com.jetug.power_armor_mod.common.util.helpers.AnimationHelper.*;
import static com.jetug.power_armor_mod.common.util.helpers.MathHelper.*;
import static net.minecraft.util.Mth.*;
import static org.apache.logging.log4j.Level.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;

public class PowerArmorEntity extends PowerArmorBase implements IAnimatable {
    public static final float ROTATION = (float) Math.PI / 180F;
    public static final int EFFECT_DURATION = 9;
    public static final int DASH_HEAT = 100;
    public static final int PUNCH_HEAT = 100;
    public static final int MAX_PUNCH_FORCE = 20;
    public static final int DASH_DURATION = 10;
    public static final int PUNCH_DURATION = 10;

    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(4, ItemStack.EMPTY);
    public final Speedometer speedometer = new Speedometer(this);

    protected boolean isJumping;
    protected float playerJumpScale;

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    private boolean isDashing = false;
    private boolean isPunching = false;
    private DashDirection dashDirection;

    public PowerArmorEntity(EntityType<? extends PowerArmorBase> type, Level worldIn) {
        super(type, worldIn);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return PowerArmorBase.createLivingAttributes()
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

        if(isServerSide && isDashing) {
            pushEntitiesAround();
        }

//        syncDataWithClient();
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
            damageArmorItem(HELMET, damageSource , damage);
            damageArmorItem(BODY_ARMOR, damageSource , damage);
            damageArmorItem(LEFT_ARM_ARMOR, damageSource , damage);
            damageArmorItem(RIGHT_ARM_ARMOR, damageSource , damage);

            if(damageSource == DamageSource.FALL) {
                damageArmorItem(LEFT_LEG_ARMOR, damageSource, damage);
                damageArmorItem(RIGHT_LEG_ARMOR, damageSource, damage);
            }
        }

        return true;
    }

    @Override
    public boolean causeFallDamage(float height, float multiplier, DamageSource damageSource) {
        if (height > 1) this.playSound(SoundEvents.HORSE_LAND, 0.4F, 1.0F);
        if (height > 4) pushEntitiesAround();

        int damage = this.calculateFallDamage(height, multiplier);
        if (damage <= 0)
            return false;

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

    @SuppressWarnings("ConstantConditions")
    @Override
    public void positionRider(Entity entity) {
        super.positionRider(entity);

        var height = getPlayerPassenger().isShiftKeyDown() ?  1.5f : 0.9f;
        var posY = getY() + getPassengersRidingOffset() + entity.getMyRidingOffset() - height;
        entity.setPos(getX(), posY, getZ());

        if (entity instanceof LivingEntity livingEntity)
            livingEntity.yBodyRot = yBodyRot;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (!isAlive()) return;
        if (isVehicle() && hasPlayerPassenger())
            travelWithPlayerPassenger(travelVector);
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

        if(itemStack.getItem() instanceof PowerArmorItem armorItem)
            armorItem.damageArmor(itemStack, (int) damage);
    }

    @Nullable
    public EquipmentBase getEquipmentItem(BodyPart part) {
        var stack = getEquipment(part);
        if(!stack.isEmpty())
            return (EquipmentBase) stack.getItem();
        return null;
    }

    public void openGUI(Player player) {
        Global.referenceMob = this;

        if (isServerSide) {
            player.openMenu(new MenuProvider() {
                @Override
                public AbstractContainerMenu createMenu(int id, Inventory menu, Player player) {
                    return new PowerArmorMenu(id, inventory, menu, PowerArmorEntity.this);
                }

                @Override
                public Component getDisplayName() {
                    return PowerArmorEntity.this.getDisplayName();
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
                    return new ArmorStationMenu2(id, inventory, menu, PowerArmorEntity.this);
                }

                @Override
                public Component getDisplayName() {
                    return PowerArmorEntity.this.getDisplayName();
                }
            });
        }
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

    public void jump(){
        playerJumpScale = 1.0F;
    }

    public void dash(DashDirection direction) {
//        if(isClientSide)
//            doServerAction(new DashAction(direction), getId());
        doHeatAction(DASH_HEAT, () -> _dash(direction));
    }

    public void push(Vec3 vector){
        setDeltaMovement(getDeltaMovement().add(vector));
    }

    public void punch(){
        punch(() -> {
            var player = getPlayerPassenger();
            player.getEyePosition();
        });
    }

    public static Entity getViewTarget(Player player) {
        var minecraft = Minecraft.getInstance();
        var cameraEntity = minecraft.getCameraEntity();

        if (cameraEntity == null || minecraft.level == null) return null;

        minecraft.getProfiler().push("pick");
        minecraft.crosshairPickEntity = null;
        double pickRange = minecraft.gameMode.getPickRange();

        var vec3 = cameraEntity.getEyePosition(1);
        boolean flag = false;
        double d1 = pickRange;

        if (minecraft.gameMode.hasFarPickRange()) {
            d1 = 6.0D;
            pickRange = d1;
        } else if (pickRange > 3.0D) flag = true;

        d1 *= d1;
        if (minecraft.hitResult != null)
            d1 = minecraft.hitResult.getLocation().distanceToSqr(vec3);

        var vec31 = cameraEntity.getViewVector(1.0F);
        var vec32 = vec3.add(vec31.x * pickRange, vec31.y * pickRange, vec31.z * pickRange);
        var aabb = cameraEntity.getBoundingBox().expandTowards(vec31.scale(pickRange)).inflate(1.0D, 1.0D, 1.0D);

        var entityHitResult = ProjectileUtil.getEntityHitResult(
                cameraEntity, vec3, vec32, aabb, (p_172770_) ->
                !p_172770_.isSpectator() && p_172770_.isPickable(), d1);

        if (entityHitResult != null) {
            var target = entityHitResult.getEntity();
            var vec33 = entityHitResult.getLocation();
            var distance = vec3.distanceToSqr(vec33);
            return target;
        }

        return null;
    }

    public void getViewTarget1(float pPartialTicks) {
        var minecraft = Minecraft.getInstance();
        var cameraEntity = Minecraft.getInstance().getCameraEntity();

        if (cameraEntity == null || minecraft.level == null) return;

        minecraft.getProfiler().push("pick");
        minecraft.crosshairPickEntity = null;
        double pickRange = minecraft.gameMode.getPickRange();
        //minecraft.hitResult = cameraEntity.pick(pickRange, pPartialTicks, false);

        var vec3 = cameraEntity.getEyePosition(pPartialTicks);
        boolean flag = false;
        double d1 = pickRange;

        if (minecraft.gameMode.hasFarPickRange()) {
            d1 = 6.0D;
            pickRange = d1;
        } else if (pickRange > 3.0D) flag = true;

        d1 *= d1;
        if (minecraft.hitResult != null)
            d1 = minecraft.hitResult.getLocation().distanceToSqr(vec3);

        var vec31 = cameraEntity.getViewVector(1.0F);
        var vec32 = vec3.add(vec31.x * pickRange, vec31.y * pickRange, vec31.z * pickRange);
        var aabb = cameraEntity.getBoundingBox().expandTowards(vec31.scale(pickRange)).inflate(1.0D, 1.0D, 1.0D);

        var entityHitResult = ProjectileUtil.getEntityHitResult(cameraEntity, vec3, vec32, aabb, (p_172770_) ->
                !p_172770_.isSpectator() && p_172770_.isPickable(), d1);

        if (entityHitResult != null) {
            var entity1 = entityHitResult.getEntity();
            var vec33 = entityHitResult.getLocation();
            var d2 = vec3.distanceToSqr(vec33);

            if (flag && d2 > 9.0D) {
                minecraft.hitResult = BlockHitResult.miss(vec33, Direction.getNearest(vec31.x, vec31.y, vec31.z), new BlockPos(vec33));
            } else if (d2 < d1 || minecraft.hitResult == null) {
                minecraft.hitResult = entityHitResult;
                if (entity1 instanceof LivingEntity || entity1 instanceof ItemFrame) {
                    minecraft.crosshairPickEntity = entity1;
                }
            }
        }

        minecraft.getProfiler().pop();

    }

    public void punchTarget(Entity target){
        if(!hasPlayerPassenger()) return;

        punch(() -> {
            var vector = getPlayerPassenger().getViewVector(1.0F);
            var force = getPunchForce();
            EntityUtils.push(target, vector.multiply(force, force, force));
        });
    }

    private float getPunchForce(){
        var charge = getAttackChargeInPercent();
        return getPercentOf(MAX_PUNCH_FORCE, charge);
    }

    private void punch(Runnable runnable){
        doHeatAction(PUNCH_HEAT, () -> {
            isPunching = true;
            timer.addCooldownTimer(PUNCH_DURATION, () -> isPunching = false);
            runnable.run();
        });
    }

    private void _dash(DashDirection direction){
        if (!(getControllingPassenger() instanceof Player player))
            return;

        isDashing = true;
        dashDirection = direction;
        timer.addCooldownTimer(DASH_DURATION, () -> isDashing = false);

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

    @Override
    public void registerControllers(AnimationData data) {
        var armController = new AnimationController<>(this, "arm_controller", 0, this::animateArms);
        var legController = new AnimationController<>(this, "leg_controller", 0, this::animateLegs);
        data.addAnimationController(armController);
        data.addAnimationController(legController);
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
            else if (isWalking()) {
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

        var move = getDeltaMovement();

        var player = getPlayerPassenger();
        var pa = getPlayerPowerArmor(player);

        if(!isDashing) {
            if (pa.isWalking()) {
                if (player.isShiftKeyDown()){
                    setAnimation(controller, "sneak_walk", LOOP);
                }
                else {
                    setAnimation(controller, "walk_legs", LOOP);
                    controller.animationSpeed = speedometer.getSpeed() * 4.0D;
                }
                return PlayState.CONTINUE;
            }
            else {
                if (player.isShiftKeyDown()) {
                    controller.setAnimation(new AnimationBuilder()
                            //.addAnimation("sneak", PLAY_ONCE)
                            .addAnimation("sneak_end", LOOP));

//                    setAnimation(controller, "sneak", PLAY_ONCE);
//                    setAnimation(controller, "sneak_end", LOOP);
                    return PlayState.CONTINUE;
                }
            }
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


    private void travelWithPlayerPassenger(Vec3 travelVector) {
        var player = getPlayerPassenger();
        setRotationMatchingPassenger(player);

        if (playerJumpScale > 0.0F && !isJumping() && onGround)
            makeJump(player);

        this.flyingSpeed = getSpeed() * 0.1F;

        if (isControlledByLocalInstance()) {
            //this.setSpeed((float)getAttributeValue(Attributes.MOVEMENT_SPEED));
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

    public boolean isPunching() {
        return isPunching;
    }
}