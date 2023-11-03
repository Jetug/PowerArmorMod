package com.jetug.chassis_core.common.foundation.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class StackUtils {
    public static final String DEFAULT = "default";
    public static final String VARIANT = "variant";
    public static final String MODIFICATION = "modification";

    public static String getVariant(ItemStack stack){
        var tag = stack.getOrCreateTag();
        return tag.contains(VARIANT) ? tag.getString(VARIANT) : DEFAULT;
    }

    public static void setVariant(ItemStack stack, String variant){
        var tag = stack.getOrCreateTag();
        tag.putString(VARIANT, variant);
        stack.setTag(tag);
    }

    public static void addMod(ItemStack stack, String variant){
        var mods = getMods(stack);
        mods.add(variant);
        setMod(stack, mods);
    }

    public static boolean hasMod(ItemStack stack, String mod){
        var mods = getMods(stack);
        return mods.contains(mod);
    }

    public static ArrayList<String> getMods(ItemStack stack){
        var tag = stack.getOrCreateTag();
        if (!tag.contains(MODIFICATION)) return new ArrayList<>();
        var listTag = (ListTag)tag.get(MODIFICATION);
        return getAsArray(listTag);
    }

    public static void setMod(ItemStack stack, Collection<String> variants){
        var tag = stack.getOrCreateTag();
        var listTag = new ListTag();

        for(var str : variants){
            var stringTag = StringTag.valueOf(str);
            listTag.add(stringTag);
        }

        tag.put(MODIFICATION, listTag);
        stack.setTag(tag);
    }

    public static void setMod(ItemStack stack, String variant){
        var tag = stack.getOrCreateTag();

        var listTag = new ListTag();
        var stringTag = StringTag.valueOf(variant);
        listTag.add(stringTag);

        tag.put(MODIFICATION, listTag);
        stack.setTag(tag);
    }

    private static ArrayList<String> getAsArray(ListTag listTag){
        var res = new ArrayList<String>();
        for(var item : listTag){
            var strTag = (StringTag)item;
            res.add(strTag.getAsString());
        }
        return res;
    }
}
