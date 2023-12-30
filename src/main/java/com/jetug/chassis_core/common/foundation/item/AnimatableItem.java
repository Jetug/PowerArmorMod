package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.json.*;
import mod.azure.azurelib.animatable.*;
import mod.azure.azurelib.core.animatable.instance.*;
import mod.azure.azurelib.renderer.*;
import net.minecraft.client.renderer.*;
import net.minecraft.world.item.*;
import net.minecraftforge.client.*;
import net.minecraftforge.common.util.*;
import javax.annotation.*;
import java.util.function.*;

import static com.jetug.chassis_core.client.ClientConfig.*;
import static com.jetug.chassis_core.client.render.utils.ResourceHelper.*;
import static mod.azure.azurelib.util.AzureLibUtil.*;

public abstract class AnimatableItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = createInstanceCache(this);
    private final Lazy<String> name = Lazy.of(() -> getResourceName(getRegistryName()));
    private final Lazy<ItemConfig> config = Lazy.of(() -> modResourceManager.getItemConfig(getName()));

//    public GeoItemRenderer<AnimatableItem> renderer;

    public AnimatableItem(Properties pProperties) {
        super(pProperties);
    }

//    abstract protected GeoItemRenderer<AnimatableItem> createRenderer();

    public String getName(){
        return name.get();
    }

    @Nullable
    public ItemConfig getConfig(){
        return config.get();
    }

//    @Override
//    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
//        super.initializeClient(consumer);
//        renderer = createRenderer();
//
//        consumer.accept(new IItemRenderProperties() {
//            @Override
//            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
//                return renderer;
//            }
//        });
//    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
