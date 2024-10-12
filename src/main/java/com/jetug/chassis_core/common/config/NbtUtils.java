
package com.jetug.chassis_core.common.config;

import com.jetug.chassis_core.common.data.json.EquipmentAttachment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

public class NbtUtils {
    public static CompoundTag serializeStringArray(ArrayList<String> array){
        var tag = new CompoundTag();
        for (var i = 0; i < array.size(); i++)
            tag.putString(String.valueOf(i), array.get(i));
        return tag;
    }

    public static String[] deserializeStringArray(CompoundTag tag){
        var array = new ArrayList<String>();

        for (var key: tag.getAllKeys()) {
            if(tag.contains(key, Tag.TAG_STRING))
                array.add(tag.getString(key));
        }

        return (String[])array.toArray();
    }

    public static <T> CompoundTag serializeSet(Set<T> array){
        var tag = new CompoundTag();
        var iterator = array.iterator();
        var i = 0;

        while (iterator.hasNext()){
            tag.putString(String.valueOf(i), iterator.next().toString());
            i++;
        }

        return tag;
    }

    public static Set<ResourceLocation> deserializeAmmoSet(CompoundTag tag){
        var array = new HashSet<ResourceLocation>();

        for (var key: tag.getAllKeys()) {
            if(tag.contains(key, Tag.TAG_STRING)) {
                array.add(ResourceLocation.tryParse(tag.getString(key)));
            }
        }

        return array;
    }

    public static <T extends INBTSerializable<?>> CompoundTag serializeArray(T[] array){
        var tag = new CompoundTag();
//        if(array instanceof INBTSerializable<?>[])
        for (var i = 0; i < array.length; i++){
            tag.put(String.valueOf(i), array[i].serializeNBT());
        }
        return tag;
    }

    public static <T> CompoundTag serializeArray(T[] array){
        var tag = new CompoundTag();
        for (var i = 0; i < array.length; i++){
            tag.putString(String.valueOf(i), array[i].toString());
        }
        return tag;
    }

//    public static <K, R extends INBTSerializable> CompoundTag serializeMap(Map<K, R> map){
//        var tag = new CompoundTag();
//
//        for (var entry : map.entrySet()) {
//            tag.put(entry.getKey().toString(), entry.getValue().serializeNBT());
//        }
//
//        return tag;
//    }

    public static <K, R> CompoundTag serializeMap(Map<K, R> map){
        var tag = new CompoundTag();

        for (var entry : map.entrySet()) {
            tag.putString(entry.getKey().toString(), entry.getValue().toString());
        }

        return tag;
    }

    public static Map<String, ResourceLocation> deserializeReaourceMap(CompoundTag tag){
        var map = new HashMap<String, ResourceLocation>();

        for (var key: tag.getAllKeys()) {
            if(tag.contains(key, Tag.TAG_STRING)) {
                var value = tag.getString(key);
                map.put(key, ResourceLocation.tryParse(value));
            }
        }

        return map;
    }

    public static EquipmentAttachment[] deserializeAttachmentArray(CompoundTag tag){
        var arrayList = new ArrayList<EquipmentAttachment>();

        for (var key: tag.getAllKeys()) {
            if(tag.contains(key, Tag.TAG_STRING)) {
                var value = new EquipmentAttachment();
                value.deserializeNBT(tag.getCompound(key));
                arrayList.add(value);
            }
        }

        return (EquipmentAttachment[]) arrayList.toArray();
    }

//    public static String[] deserializeStringArray(CompoundTag tag){
//        var map = new ArrayList<String>();
//
//        for (var key: tag.getAllKeys()) {
//            if(tag.contains(key, Tag.TAG_STRING)) {
//                var value = tag.getString(key);
//                map.add(value);
//            }
//        }
//
//        return (String[])map.toArray();
//    }
}
