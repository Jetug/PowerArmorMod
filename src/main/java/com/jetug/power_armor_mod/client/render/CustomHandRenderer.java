package com.jetug.power_armor_mod.client.render;

import com.jetug.power_armor_mod.client.render.renderers.*;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.model.*;

import static com.jetug.power_armor_mod.client.render.renderers.item.HandRenderer.*;

public class CustomHandRenderer extends CustomGeoRenderer<IAnimatable> {
    private static CustomHandRenderer handRenderer;
    private static HandAmimator handAmimator;

    static {
        AnimationController.addModelFetcher(animatable -> animatable instanceof HandAmimator? HAND_MODEL: null);
    }

    public CustomHandRenderer(AnimatedGeoModel<IAnimatable> model) {
        super(model);
    }

    public static void registerHandRenderer(){
        handRenderer = new CustomHandRenderer(HAND_MODEL);
        handAmimator = new HandAmimator();
    }

    public static CustomHandRenderer getHandRenderer(){
        return handRenderer;
    }

    public static HandAmimator getHandAmimator(){
        return handAmimator;
    }
}
