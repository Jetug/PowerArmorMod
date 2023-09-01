package com.jetug.chassis_core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.jetug.chassis_core.common.util.helpers.PlayerUtils.*;
import static com.jetug.chassis_core.common.util.helpers.AnimationHelper.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;
import static software.bernie.geckolib3.util.GeckoLibUtil.*;

public class HandEntity implements IAnimatable {
    public AnimationFactory factory = createFactory(this);
    public LocalPlayer player;

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
        player = Minecraft.getInstance().player;
    }

    @SuppressWarnings("ConstantConditions")
    protected <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}