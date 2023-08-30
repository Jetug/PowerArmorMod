package com.jetug.chassis_core.common.foundation.item;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

import java.util.function.Consumer;

import static software.bernie.geckolib3.util.GeckoLibUtil.createFactory;

public abstract class AnimatableItem extends Item implements IAnimatable{
    public AnimationFactory factory = createFactory(this);
    public GeoItemRenderer renderer;

    public AnimatableItem(Properties pProperties) {
        super(pProperties);
    }

    abstract protected GeoItemRenderer createRenderer();

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        renderer = createRenderer();

        consumer.accept(new IItemRenderProperties() {

            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
