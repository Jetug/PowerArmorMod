package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.client.render.renderers.item.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import net.minecraftforge.client.*;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.manager.*;

import java.util.function.Consumer;

import static software.bernie.geckolib3.util.GeckoLibUtil.createFactory;

public class HandItem extends Item implements IAnimatable {
    public AnimationFactory factory = createFactory(this);
    public HandRenderer renderer;

    public HandRenderer getRenderer(){
        return renderer;
    }

    public HandItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        renderer = new HandRenderer();

        consumer.accept(new IItemRenderProperties() {

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimationData data) {

    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
