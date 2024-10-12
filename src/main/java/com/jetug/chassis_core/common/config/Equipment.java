package com.jetug.chassis_core.common.config;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.jetug.chassis_core.common.config.holders.BodyPart;
import com.jetug.chassis_core.common.data.annotation.Optional;
import com.jetug.chassis_core.common.data.json.EquipmentAttachment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.jetug.chassis_core.common.foundation.item.StackUtils.DEFAULT;

public class Equipment implements INBTSerializable<CompoundTag>{
    public static final String TYPE = "Type";
    public static final String PARENT = "Parent";

//    private final Lazy<ResourceLocation> modelLocation = Lazy.of(this::getModelResource);
//    private final Lazy<Map<String, ResourceLocation>> textureLocation = Lazy.of(this::initTextureResource);

    public String parent;
    public BodyPart part;
    public ResourceLocation model;
    public Map<String, ResourceLocation> texture;
    public int[] uv;
    public String[] hide = new String[0];
    public EquipmentAttachment[] attachments;
    public String[] mods = new String[0];

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(PARENT, this.parent);
        tag.putString("Model", this.model.toString());
        tag.put("Texture", NbtUtils.serializeMap(texture));
        tag.putIntArray("UV", uv);
        tag.putString("Part", part.getId().toString());
        tag.put("Hide", NbtUtils.serializeArray(hide));
        tag.put("Attachments", NbtUtils.serializeArray(attachments));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        if (tag.contains(PARENT, Tag.TAG_ANY_NUMERIC)) {
            this.parent = tag.getString(PARENT);
        }
        if (tag.contains("Model", Tag.TAG_STRING)) {
            this.model = ResourceLocation.tryParse(tag.getString("Model"));
        }
        if (tag.contains("Texture", Tag.TAG_COMPOUND)) {
            this.texture = NbtUtils.deserializeReaourceMap(tag.getCompound("Texture"));
        }
        if (tag.contains("UV", Tag.TAG_INT_ARRAY)) {
            this.uv = tag.getIntArray("UV");
        }
        if (tag.contains("Part", Tag.TAG_STRING)) {
            this.part = BodyPart.getPart(tag.getString("Part"));
        }
        if (tag.contains("Hide", Tag.TAG_COMPOUND)) {
            this.hide = NbtUtils.deserializeStringArray(tag.getCompound("Hide"));
        }
        if (tag.contains("Attachments", Tag.TAG_ANY_NUMERIC)) {
            this.attachments = NbtUtils.deserializeAttachmentArray(tag.getCompound("Attachments"));
        }
    }

    public Equipment copy() {
        var projectile = new Equipment();
        projectile.parent = this.parent;
        projectile.model = this.model;
        projectile.texture = this.texture;
        projectile.uv = this.uv;
        projectile.part = this.part;
        projectile.hide = this.hide;
        projectile.attachments = this.attachments;
        return projectile;
    }

//    public JsonObject toJsonObject() {
//        Preconditions.checkArgument(this.damage >= 0.0F, "Damage must be more than or equal to zero");
//        Preconditions.checkArgument(this.size >= 0.0F, "Projectile size must be more than or equal to zero");
//        Preconditions.checkArgument(this.speed >= 0.0, "Projectile speed must be more than or equal to zero");
//        Preconditions.checkArgument(this.life > 0, "Projectile life must be more than zero");
//        Preconditions.checkArgument(this.trailLengthMultiplier >= 0.0, "Projectile trail length multiplier must be more than or equal to zero");
//        JsonObject object = new JsonObject();
//        if (this.visible) object.addProperty("visible", true);
//        object.addProperty("damage", this.damage);
//        object.addProperty("size", this.size);
//        object.addProperty("speed", this.speed);
//        object.addProperty("life", this.life);
//        object.addProperty("type", this.type.toString());
//
//        if (this.gravity) object.addProperty("gravity", true);
//        if (this.damageReduceOverLife) object.addProperty("damageReduceOverLife", this.damageReduceOverLife);
//        if (this.magazineMode) object.addProperty("magazineMode", this.magazineMode);
//        if (this.trailColor != 0xFFD289) object.addProperty("trailColor", this.trailColor);
//        if (this.trailLengthMultiplier != 1.0)
//            object.addProperty("trailLengthMultiplier", this.trailLengthMultiplier);
//        return object;
//    }



    public static Equipment create(CompoundTag tag) {
        var ammo = new Equipment();
        ammo.deserializeNBT(tag);
        return ammo;
    }

    public String getParent() {
        return parent;
    }

    public ResourceLocation getModel() {
        return model;
    }

    public ResourceLocation getTexture(String variant) {
        return texture.getOrDefault(variant, texture.get(DEFAULT));
    }

    public Map<String, ResourceLocation> getTextures() {
        return texture;
    }

    public int[] getUv() {
        return uv;
    }

    public BodyPart getPart() {
        return part;
    }

    public String[] getHide() {
        return hide;
    }

    public EquipmentAttachment[] getAttachments() {
        return attachments;
    }

    public String[] getMods() {
        return mods;
    }

    @Nullable
    public Collection<String> getArmorBone(String chassisBone) {
        var result = new ArrayList<String>();
        for (var attachment : attachments) {

            if (Objects.equals(attachment.frame, chassisBone))
                result.add(attachment.armor);

        }
        return result;
    }

    //    public static class Builder {
//        private final Equipment ammo;
//
//        private Builder() {
//            this.ammo = new Ammo();
//        }
//
//        private Builder(Ammo ammo) {
//            this.ammo = ammo.copy();
//        }
//
//        public static Ammo.Builder create() {
//            return new Ammo.Builder();
//        }
//
//        public static Ammo.Builder create(Ammo ammo) {
//            return new Ammo.Builder(ammo);
//        }
//
//        public Ammo build() {
//            return this.ammo.copy(); //Copy since the builder could be used again
//        }
//
//        public Ammo.Builder setProjectileVisible(ResourceLocation id, boolean visible) {
//            this.ammo.visible = visible;
//            return this;
//        }
//
//        public Ammo.Builder setProjectileSize(ResourceLocation id, float size) {
//            this.ammo.size = size;
//            return this;
//        }
//
//        public Ammo.Builder setProjectileSpeed(ResourceLocation id, double speed) {
//            this.ammo.speed = speed;
//            return this;
//        }
//
//        public Ammo.Builder setProjectileLife(ResourceLocation id, int life) {
//            this.ammo.life = life;
//            return this;
//        }
//
//        public Ammo.Builder setProjectileAffectedByGravity(ResourceLocation id, boolean gravity) {
//            this.ammo.gravity = gravity;
//            return this;
//        }
//
//        public Ammo.Builder setProjectileTrailColor(ResourceLocation id, int trailColor) {
//            this.ammo.trailColor = trailColor;
//            return this;
//        }
//
//        public Ammo.Builder setProjectileTrailLengthMultiplier(ResourceLocation id, int trailLengthMultiplier) {
//            this.ammo.trailLengthMultiplier = trailLengthMultiplier;
//            return this;
//        }
//
//        public Ammo.Builder setDamage(ResourceLocation id, float damage) {
//            this.ammo.damage = damage;
//            return this;
//        }
//
//        public Ammo.Builder setReduceDamageOverLife(ResourceLocation id, boolean damageReduceOverLife) {
//            this.ammo.damageReduceOverLife = damageReduceOverLife;
//            return this;
//        }
//
//        public Ammo.Builder setMagazineMode(ResourceLocation id, boolean magazineMode) {
//            this.ammo.magazineMode = magazineMode;
//            return this;
//        }
//    }
}
