package com.jetug.power_armor_mod.client.render.renderers;

import com.jetug.power_armor_mod.client.model.item.HandModel;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.model.*;

public class CustomHandRenderer extends CustomGeoRenderer<IAnimatable> {
    private static CustomHandRenderer handRenderer;
    private static HandEntity handAmimator;
    public static final HandModel HAND_MODEL = new HandModel();

    static {
        AnimationController.addModelFetcher(animatable -> animatable instanceof HandEntity ? HAND_MODEL: null);
    }

    public CustomHandRenderer(AnimatedGeoModel<IAnimatable> model) {
        super(model);
    }

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
