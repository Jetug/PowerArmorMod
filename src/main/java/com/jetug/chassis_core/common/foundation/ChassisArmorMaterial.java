package com.jetug.chassis_core.common.foundation;

import com.jetug.chassis_core.common.data.enums.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.function.Supplier;

public class ChassisArmorMaterial {
    private static final int[] HEALTH_PER_SLOT = new int[]{11, 16, 13, 13};

    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final float toughness;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    private final int[] craftPerSlot;

    public ChassisArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections,
                                float toughness, int enchantmentValue,
                                SoundEvent sound, float knockbackResistance,
                                Supplier<Ingredient> p_40481_, int[] craftPerSlot) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.toughness = toughness;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(p_40481_);
        this.craftPerSlot = craftPerSlot;
    }

    public int getDurabilityForSlot(BodyPart bodyPart) {
        return HEALTH_PER_SLOT[bodyPart.getArmorPartId()] * this.durabilityMultiplier;
    }

    public int getDefenseForSlot(BodyPart bodyPart) {
        return slotProtections[bodyPart.getArmorPartId()];
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    public String getName() {
        return this.name;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    public int getCraftPerSlotForSlot(BodyPart bodyPart) {
        return craftPerSlot[bodyPart.getArmorPartId()];
    }
}