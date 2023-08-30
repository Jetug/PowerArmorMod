package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public abstract class DamageableItem extends ChassisEquipment {
    private static final String DAMAGE_KEY = "Damage";

    public DamageableItem(Properties pProperties, BodyPart part) {
        super(pProperties, part);
    }

    public DamageableItem(BodyPart part) {
        super(part);
    }

    public static int getItemDamage(ItemStack itemStack) {
        if(itemStack.hasTag()) {
            CompoundTag nbt = itemStack.getOrCreateTag();
            return nbt.getInt(DAMAGE_KEY);
        }
        else return 0;
    }

    public static void setItemDamage(ItemStack head, int totalDamage)
    {
        CompoundTag nbt = head.getOrCreateTag();
        nbt.putInt(DAMAGE_KEY, totalDamage);
    }
}
