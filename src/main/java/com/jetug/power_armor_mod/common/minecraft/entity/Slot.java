package com.jetug.power_armor_mod.common.minecraft.entity;

import com.jetug.power_armor_mod.client.render.ResourceHelper;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.enums.EquipmentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.util.ArrayList;

public abstract class Slot {
    protected BodyPart part;
    protected EquipmentType type;
    public final IPowerArmor parent;

    public Slot(IPowerArmor parent, BodyPart part, EquipmentType type){
        this.parent = parent;
        this.part = part;
        this.type = type;
    }

    public ArrayList<Tuple<String, String>> getAttachments(){
        return ResourceHelper.getAttachments(part);
    }

    public BodyPart getArmorPart() {
        return part;
    }

    public EquipmentType getType() {
        return type;
    }

    public void setType(EquipmentType type) {
        this.type = type;
    }

    @Nullable
    public ResourceLocation getModel() {
        return ResourceHelper.getModel(part, type);
    }

    @Nullable
    public ResourceLocation getTexture() {
        return ResourceHelper.getTexture(part, type);
    }
}
