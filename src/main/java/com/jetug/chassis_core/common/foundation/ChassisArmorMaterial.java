package com.jetug.chassis_core.common.foundation;

import com.jetug.chassis_core.common.data.enums.*;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.function.Supplier;

import static com.jetug.chassis_core.common.data.enums.BodyPart.*;

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

    private int getPartId(String part){
        var i = 0;
        return switch (part){
            case HELMET          -> 0;
            case BODY_ARMOR      -> 1;
            case LEFT_ARM_ARMOR  -> 2;
            case RIGHT_ARM_ARMOR -> 3;
            case LEFT_LEG_ARMOR  -> 4;
            default -> 0;
        };
    }

    public int getDurabilityForSlot(String bodyPart) {
        return HEALTH_PER_SLOT[getPartId(bodyPart)] * this.durabilityMultiplier;
    }

    public int getDefenseForSlot(String bodyPart) {
        return slotProtections[getPartId(bodyPart)];
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

    public int getCraftPerSlotForSlot(String bodyPart) {
        return craftPerSlot[getPartId(bodyPart)];
    }
}