package com.jetug.power_armor_mod.common.util.helpers;

import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;

public class AnimationHelper {
    public static void setAnimation(AnimationController<?> controller, String name, ILoopType loopType){
        controller.setAnimation(new AnimationBuilder().addAnimation(name, loopType));
    }
}
