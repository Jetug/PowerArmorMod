package com.jetug.chassis_core.common.data.json;

import com.jetug.chassis_core.common.config.NbtUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public class EquipmentAttachment implements INBTSerializable<CompoundTag> {
    public AttachmentMode mode;
    public String frame;
    public String armor;


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString("mode", this.mode.toString());
        tag.putString("frame", this.frame);
        tag.putString("armor", this.armor);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains("mode", Tag.TAG_STRING)) {
            this.mode = AttachmentMode.valueOf(tag.getString("mode"));
        }
        if (tag.contains("frame", Tag.TAG_STRING)) {
            this.frame = tag.getString("frame");
        }
        if (tag.contains("armor", Tag.TAG_STRING)) {
            this.armor = tag.getString("armor");
        }
    }
}
