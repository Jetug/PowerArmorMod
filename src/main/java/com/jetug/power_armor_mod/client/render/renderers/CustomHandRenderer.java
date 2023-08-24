package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.HandModel;
import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.util.extensions.PlayerExtension;
import com.mojang.math.Vector3d;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.*;
import software.bernie.geckolib3.resource.GeckoLibCache;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.getPlayerChassis;

public class CustomHandRenderer extends CustomGeoRenderer<IAnimatable> {
    public static final String RIGHT_HAND_BONE = "right_lower_arm";
    private static CustomHandRenderer handRenderer;
    private static HandEntity handAmimator;
    public static final HandModel HAND_MODEL = new HandModel();

    static {
        AnimationController.addModelFetcher(animatable -> animatable instanceof HandEntity ? HAND_MODEL: null);
    }

    public CustomHandRenderer(AnimatedGeoModel<IAnimatable> model) {
        super(model);
    }

//    @Override
//    public GeoModel getModel() {
//        var chassis = PlayerExtension.getPlayerChassis();
//        var name = chassis.getRegistryName();
//        var location = new ResourceLocation(Global.MOD_ID, "geo/chassis/" + name + ".geo.json");
//        var chassisModel = GeckoLibCache.getInstance().getGeoModels().get(location);
//        var handModel = new GeoModel();
//
//        if(chassisModel != null) {
//            chassisModel.getBone(RIGHT_HAND_BONE).ifPresent((handBone) -> {
//                var pos = chassis.position();
//                handBone.setModelPosition(new Vector3d(0,0,0));
//                handModel.topLevelBones.add(handBone);
//            });
//        }
//
//        return handModel;
//    }

    public static void registerHandRenderer(){
        handRenderer = new CustomHandRenderer(HAND_MODEL);
        handAmimator = new HandEntity();
    }

    public static CustomHandRenderer getHandRenderer(){
        return handRenderer;
    }

    public static HandEntity getHandAmimator(){
        return handAmimator;
    }
}
