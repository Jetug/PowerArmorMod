package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IJumpingMount;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class HitboxTestEntity extends PigEntity implements IJumpingMount, IPowerArmor   {
    public HitboxTestEntity(EntityType<? extends PigEntity> type, World world) {
        super(type, world);
    }

    public static AttributeModifierMap.MutableAttribute createAttributes() {
        return CreatureEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.20D);
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
}
