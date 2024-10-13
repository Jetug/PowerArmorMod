package com.jetug.chassis_core.common.foundation;

import com.jetug.chassis_core.common.config.holders.BodyPart;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;
import java.util.function.Supplier;

import static com.jetug.chassis_core.common.config.holders.BodyPart.*;
import static com.jetug.chassis_core.common.data.constants.ChassisPart.*;

public class ChassisArmorMaterial {
    private static final int[] HEALTH_PER_SLOT = new int[]{11, 16, 13, 13};

    private static final Map<BodyPart, Integer> partId = Map.of(
            BodyPart.HEAD       , 0,
            BodyPart.BODY       , 1,
            BodyPart.LEFT_ARM   , 2,
            BodyPart.RIGHT_ARM  , 2,
            BodyPart.LEFT_LEG   , 3,
            BodyPart.RIGHT_LEG  , 3
    );

    private final String name;
    private final int durabilityMultiplier;
    private final int[] slotProtections;
    private final float toughness;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    public ChassisArmorMaterial(String name, int durabilityMultiplier, int[] slotProtections,
                                float toughness, int enchantmentValue,
                                SoundEvent sound, float knockbackResistance,
                                Supplier<Ingredient> p_40481_) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.slotProtections = slotProtections;
        this.toughness = toughness;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(p_40481_);
    }

    protected int getPartId(BodyPart part) {
        return partId.getOrDefault(part,0);
    }

    public int getDurabilityForSlot(BodyPart bodyPart) {
        return HEALTH_PER_SLOT[getPartId(bodyPart)] * this.durabilityMultiplier;
    }

    public int getDefenseForSlot(BodyPart bodyPart) {
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
}