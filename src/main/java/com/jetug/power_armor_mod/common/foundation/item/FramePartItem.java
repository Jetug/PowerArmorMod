package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.client.render.renderers.item.FramePartRenderer;
import com.jetug.power_armor_mod.common.data.enums.*;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.IItemRenderProperties;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.function.Consumer;

import static software.bernie.geckolib3.util.GeckoLibUtil.createFactory;

public class FramePartItem extends ChassisEquipment implements IAnimatable {
    public AnimationFactory factory = createFactory(this);
    public final BodyPart bodyPart;
    public FramePartRenderer renderer;

    public FramePartItem(BodyPart bodyPart) {
        super(bodyPart);
        this.bodyPart = bodyPart;
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        renderer = new FramePartRenderer();

        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimationData data) {}

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
