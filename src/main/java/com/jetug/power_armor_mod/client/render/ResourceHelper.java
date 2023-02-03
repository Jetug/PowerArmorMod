package com.jetug.power_armor_mod.client.render;

import com.google.gson.Gson;
import com.jetug.power_armor_mod.PowerArmorMod;
import com.jetug.power_armor_mod.common.util.annotations.Model;
import com.jetug.power_armor_mod.common.util.annotations.Texture;
import com.jetug.power_armor_mod.common.util.constants.Global;
import com.jetug.power_armor_mod.common.util.constants.Resources;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import com.jetug.power_armor_mod.common.util.enums.EquipmentType;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import software.bernie.geckolib3.file.AnimationFile;

import javax.annotation.Nullable;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.jetug.power_armor_mod.common.util.constants.Bones.*;
import static com.jetug.power_armor_mod.common.util.constants.Resources.*;
import static com.jetug.power_armor_mod.common.util.enums.BodyPart.*;
import static java.nio.file.Files.newBufferedReader;

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
    }

    @Nullable
    public static ResourceLocation getTexture(BodyPart part, EquipmentType type) {
        Field[] fields = Resources.class.getFields();
        for (Field field: fields) {
            if(field.isAnnotationPresent(Texture.class)){
                Texture texture = field.getAnnotation(Texture.class);
                if(texture.ArmorPart() == part && texture.Type() == type){
                    ResourceLocation resource = null;
                    try {
                        resource = (ResourceLocation)field.get(null);
                    }
                    catch (IllegalAccessException ignored) {}
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

    public static ArmorPartSettings[] getAttachments2(BodyPart bodyPart){
        if(bodyPart == BODY){
            try {
                var files = GetFiles();
                var rl = new ResourceLocation(Global.MOD_ID, "configs/body.json");
                var path = rl.getPath();
                var np = rl.getNamespace();

                var resourceManager = Minecraft.getInstance().getResourceManager();
                //var test = resourceManager.listResources().getResources(new ResourceLocation(Global.MOD_ID,"configs"));

                //Map<ResourceLocation, AnimationFile> animations = new Object2ObjectOpenHashMap<>();
                var rss = resourceManager.listResources("configs", fileName -> fileName.endsWith(".json"));

                var readIn = getBufferedReader(resourceManager.getResource(rl).getInputStream());

//                var readIn = new BufferedReader(new InputStreamReader(
//                                Minecraft.class.getClassLoader().getResourceAsStream("/assets/power_armor_mod/configs/body.json"), "UTF-8"));
                Gson gson = new Gson();
                ArmorPartSettings abilities = gson.fromJson(readIn, ArmorPartSettings.class);
                int i = 0;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private static ArrayList<Path> GetFiles(){
        FileSystem filesystem = null;

        var resPath = "/assets/" + Global.MOD_ID + "/configs/";

        URL url = PowerArmorMod.class.getResource(resPath);

        var paths = new ArrayList<Path>();

        try
        {
            if (url != null)
            {
                URI uri = url.toURI();
                Path path = null;

                if ("file".equals(uri.getScheme()))
                {
                    path = Paths.get(PowerArmorMod.class.getResource(resPath).toURI());
                }
                else
                {

                    filesystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    path = filesystem.getPath(resPath);

                }

                var it = Files.walk(path).iterator();

                while(it.hasNext()) {
                    //System.out.println(it.next());
                    paths.add(it.next());
                }
            }
            return paths;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return paths;
    }

    public static BufferedReader getBufferedReader(InputStream stream){
        return new BufferedReader(new InputStreamReader(stream));
    }


    public static ArrayList<Tuple<String, String>> getAttachments(BodyPart bodyPart){

        getAttachments2(BODY);

        ArrayList<Tuple<String, String>> boneList = new ArrayList<>();
        
        switch (bodyPart){
            case HEAD:
                boneList.add(new Tuple<>(HEAD_BONE_NAME, "helmet"));
                break;
            case BODY:
                boneList.add(new Tuple<>("body_top"      , "body_top_armor"         ));
                boneList.add(new Tuple<>("body_bottom"   , "body_bottom_armor"      ));
                boneList.add(new Tuple<>("front_plate"   , "front_plate_armor"      ));
                boneList.add(new Tuple<>("back_plate"    , "back_plate_armor"       ));
                break;
            case LEFT_ARM:
                boneList.add(new Tuple<>("left_upper_arm", "left_shoulder_armor"    ));
                boneList.add(new Tuple<>("left_upper_arm", "left_upper_arm_armor"   ));
                boneList.add(new Tuple<>("left_lower_arm", "left_forearm_armor"     ));
                break;
            case RIGHT_ARM:
                boneList.add(new Tuple<>("right_upper_arm", "right_shoulder_armor"  ));
                boneList.add(new Tuple<>("right_upper_arm", "right_upper_arm_armor" ));
                boneList.add(new Tuple<>("right_lower_arm", "right_forearm_armor"   ));
                break;
            case LEFT_LEG:
                boneList.add(new Tuple<>("left_upper_leg", "left_upper_leg_armor"   ));
                boneList.add(new Tuple<>("left_lower_leg", "left_knee"              ));
                boneList.add(new Tuple<>("left_lower_leg", "left_lower_leg_armor"   ));
                boneList.add(new Tuple<>("left_shoe"     , "left_shoe_armor"        ));
                boneList.add(new Tuple<>("left_toes"     , "left_toes_armor"        ));
                break;
            case RIGHT_LEG:
                boneList.add(new Tuple<>("right_upper_leg", "right_upper_leg_armor" ));
                boneList.add(new Tuple<>("right_lower_leg", "right_knee"            ));
                boneList.add(new Tuple<>("right_lower_leg", "right_lower_leg_armor" ));
                boneList.add(new Tuple<>("right_shoe"     , "right_shoe_armor"      ));
                boneList.add(new Tuple<>("right_toes"     , "right_toes_armor"      ));
                break;
        }
        return boneList;
    }
}
