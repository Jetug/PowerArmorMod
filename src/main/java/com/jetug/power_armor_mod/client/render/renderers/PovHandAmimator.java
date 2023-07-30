package com.jetug.power_armor_mod.client.render.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.*;
import static com.jetug.power_armor_mod.common.util.helpers.AnimationHelper.*;
import static software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes.*;
import static software.bernie.geckolib3.util.GeckoLibUtil.*;

public class PovHandAmimator implements IAnimatable {
    public AnimationFactory factory = createFactory(this);
    public Minecraft minecraft;
    public LocalPlayer player;

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));

        minecraft = Minecraft.getInstance();
        player = minecraft.player;
    }

    @SuppressWarnings("ConstantConditions")
    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        if(!isWearingPowerArmor()) return PlayState.STOP;

        var controller = event.getController();
        var armor = getPlayerPowerArmor();
        controller.animationSpeed = 1;

        if(armor.isPunching()){
            controller.animationSpeed = 2;
            setAnimation(controller,"punch", PLAY_ONCE);
        }
        else if(armor.isMaxCharge()){
            setAnimation(controller,"punch_max_charge", LOOP);
        }
        else if(armor.isChargingAttack()){
            controller.animationSpeed = 0.3;
            setAnimation(controller,"punch_charge", LOOP);
        }
        else if(player.swinging){
            controller.animationSpeed = 3;
            setAnimation(controller,"hit", LOOP);
        }
        else if(armor.isWalking()){
            setAnimation(controller,"walk", LOOP);
        }
        else {
            setAnimation(controller,"idle", LOOP);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}