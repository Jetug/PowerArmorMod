package com.jetug.power_armor_mod.client.render;

import com.jetug.power_armor_mod.common.util.annotations.Model;
import com.jetug.power_armor_mod.common.util.annotations.Texture;
import com.jetug.power_armor_mod.common.util.constants.Resources;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.enums.EquipmentType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import static com.jetug.power_armor_mod.common.util.constants.Constants.BODY_BONE_NAME;
import static com.jetug.power_armor_mod.common.util.constants.Constants.HEAD_BONE_NAME;
import static com.jetug.power_armor_mod.common.util.constants.Resources.HELMET_MODEL_LOCATION;
import static com.jetug.power_armor_mod.common.util.constants.Resources.HELMET_TEXTURE_LOCATION;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.HEAD;
import static com.jetug.power_armor_mod.common.util.enums.EquipmentType.STANDARD;

public class ResourceHelper {

    public HashMap<BodyPart, HashMap<EquipmentType, Tuple<ResourceLocation, ResourceLocation>>> ResourcesMap = FillResourcesMap();

    public static ArrayList<ArrayList<ResourceLocation>> models;

    private HashMap<BodyPart, HashMap<EquipmentType, Tuple<ResourceLocation, ResourceLocation>>> FillResourcesMap(){
        HashMap<BodyPart, HashMap<EquipmentType, Tuple<ResourceLocation, ResourceLocation>>> result = new HashMap<>();

        HashMap<EquipmentType, Tuple<ResourceLocation, ResourceLocation>> head = new HashMap<>();
        head.put(EquipmentType.STANDARD, new Tuple(HELMET_MODEL_LOCATION, HELMET_TEXTURE_LOCATION));
        result.put(BodyPart.HEAD, head);

        HashMap<EquipmentType, Tuple<ResourceLocation, ResourceLocation>> body = new HashMap<>();

        return result;
    }

    public static void register(){
        fillModels();
    }

    private static void fillModels(){
        ArrayList<ArrayList<ResourceLocation>> parts = new ArrayList<>();
        for (BodyPart part : BodyPart.values()) {
            ArrayList<ResourceLocation> types = new ArrayList<>();
            for (EquipmentType type : EquipmentType.values()) {
                types.add(loadModel(part, type));
            }
            parts.add(types);
        }
        models = parts;
    }

    @Nullable
    public static ResourceLocation getModel(BodyPart part, EquipmentType type) {
        return models.get(part.getId()).get(type.getId());

//        Field[] fields = Resources.class.getFields();
//        for (Field field: fields) {
//            if(field.isAnnotationPresent(Model.class)){
//                Model model = field.getAnnotation(Model.class);
//                if(model.ArmorPart() == part && model.Type() == type){
//                    ResourceLocation resource;
//                    try {
//                        resource = (ResourceLocation)field.get(null);
//                    }
//                    catch (IllegalAccessException e) {
//                        return null;
//                    }
//                    return resource;
//                }
//            }
//        }
//        return null;
    }

    @Nullable
    public static ResourceLocation getTexture(BodyPart part, EquipmentType type) {
        Field[] fields = Resources.class.getFields();
        for (Field field: fields) {
            if(field.isAnnotationPresent(Texture.class)){
                Texture texture = field.getAnnotation(Texture.class);
                if(texture.ArmorPart() == part && texture.Type() == type){
                    ResourceLocation resource = null;
                    try { resource = (ResourceLocation)field.get(null); } catch (IllegalAccessException e) {}
                    return resource;
                }
            }
        }
        return null;
    }

    @Nullable
    public static ResourceLocation loadModel(BodyPart part, EquipmentType type) {
        Field[] fields = Resources.class.getFields();
        for (Field field: fields) {
            if(field.isAnnotationPresent(Model.class)){
                Model model = field.getAnnotation(Model.class);
                if(model.ArmorPart() == part && model.Type() == type){
                    ResourceLocation resource;
                    try {
                        resource = (ResourceLocation)field.get(null);
                    }
                    catch (IllegalAccessException e) {
                        return null;
                    }
                    return resource;
                }
            }
        }
        return null;
    }

    @Nullable
    public static ResourceLocation loadTexture(BodyPart part, EquipmentType type) {
        Field[] fields = Resources.class.getFields();
        for (Field field: fields) {
            if(field.isAnnotationPresent(Texture.class)){
                Texture texture = field.getAnnotation(Texture.class);
                if(texture.ArmorPart() == part && texture.Type() == type){
                    ResourceLocation resource = null;
                    try { resource = (ResourceLocation)field.get(null); } catch (IllegalAccessException e) {}
                    return resource;
                }
            }
        }
        return null;
    }

    public static ArrayList<Tuple<String, String>> getAttachments(BodyPart bodyPart){
        ArrayList<Tuple<String, String>> boneList = new ArrayList<>();
        switch (bodyPart){
            case HEAD:
                boneList.add(new Tuple<>(HEAD_BONE_NAME, "helmet"));
                break;
            case BODY:
                boneList.add(new Tuple<>(BODY_BONE_NAME, "body_top_armor"));
                break;
            case LEFT_ARM:
                boneList.add(new Tuple<>("left_upper_arm", "left_shoulder_armor"   ));
                boneList.add(new Tuple<>("left_upper_arm", "left_upper_arm_armor"  ));
                boneList.add(new Tuple<>("left_lower_arm", "left_forearm_armor"    ));
                break;
            case RIGHT_ARM:
                boneList.add(new Tuple<>("right_upper_arm", "right_shoulder_armor" ));
                boneList.add(new Tuple<>("right_upper_arm", "right_upper_arm_armor"));
                boneList.add(new Tuple<>("right_lower_arm", "right_forearm_armor"  ));
                break;
            case LEFT_LEG:
                boneList.add(new Tuple<>("left_upper_leg", "left_upper_leg_armor"  ));
                boneList.add(new Tuple<>("left_lower_leg", "left_knee"             ));
                boneList.add(new Tuple<>("left_lower_leg", "left_lower_leg_armor"  ));
                break;
            case RIGHT_LEG:
                boneList.add(new Tuple<>("right_upper_leg", "right_upper_leg_armor"));
                boneList.add(new Tuple<>("right_lower_leg", "right_knee"           ));
                boneList.add(new Tuple<>("right_lower_leg", "right_lower_leg_armor"));
                break;
        }
        return boneList;
    }
}
