package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.common.capability.data.IPlayerData;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.common.capability.data.DataManager.getPlayerData;

public class HitboxTestEntity extends AnimalEntity implements IAnimatable, IJumpingMount, IPowerArmor   {
    public HitboxTestEntity(EntityType<? extends AnimalEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D);
    }

    @Override
    protected void registerGoals() {}


    public void doPlayerRide(PlayerEntity player) {
        if (!this.level.isClientSide) {
            player.yRot = this.yRot;
            player.xRot = this.xRot;
            player.startRiding(this);

            IPlayerData data = getPlayerData(player);
            data.setIsInPowerArmor(true);
            player.sendMessage(new StringTextComponent("" + data.getIsInPowerArmor()), this.getUUID());
        }
    }

    @Override
    public ActionResultType mobInteract(PlayerEntity player, Hand p_230254_2_) {
        this.doPlayerRide(player);
        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    @Override
    public boolean isInvisible() {
        ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        PointOfView pov = Minecraft.getInstance().options.getCameraType();

        if (hasPassenger(clientPlayer) && pov == PointOfView.FIRST_PERSON)
            return true;
        return super.isInvisible();
    }

    @Override
    public void positionRider(Entity entity) {
        super.positionRider(entity);

        float posX = MathHelper.sin(this.yBodyRot * ((float) Math.PI / 180F));
        float posZ = MathHelper.cos(this.yBodyRot * ((float) Math.PI / 180F));
        double posXZ = 0.1;
        double posY = 0.9;
        entity.setPos(this.getX() + (posXZ * posX),
                this.getY() + this.getPassengersRidingOffset() + entity.getMyRidingOffset() - posY,
                this.getZ() - (posXZ * posZ));
        if (entity instanceof LivingEntity) {
            ((LivingEntity) entity).yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public float getArmorDurability(BodyPart bodyPart) {
        return 0;
    }

    @Override
    public void setArmorDurability(BodyPart bodyPart, float value) {

    }

    @Override
    public void damageArmor(BodyPart bodyPart, float damage) {

    }

    @Override
    public void onPlayerJump(int p_110206_1_) {

    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    public void handleStartJump(int p_184775_1_) {

    }

    @Override
    public void handleStopJump() {

    }

    private final AnimationFactory factory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData data) {
        AnimationController<HitboxTestEntity> controller = new AnimationController<>(this, "controller", 0, this::predicate);
        data.addAnimationController(controller);
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if(event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", true));
            event.getController().animationSpeed = 4.0D;
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }

    @Nullable
    @Override
    public AgeableEntity getBreedOffspring(ServerWorld p_241840_1_, AgeableEntity p_241840_2_) {
        return null;
    }
}
