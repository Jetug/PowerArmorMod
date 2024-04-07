package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.json.ItemConfig;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static com.jetug.chassis_core.client.ClientConfig.modResourceManager;
import static com.jetug.chassis_core.client.render.utils.ResourceHelper.getResourceName;
import static mod.azure.azurelib.util.AzureLibUtil.createInstanceCache;

public abstract class AnimatableItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = createInstanceCache(this);
    private final Lazy<String> name = Lazy.of(() -> getResourceName(ForgeRegistries.ITEMS.getKey(this)));
    private final Lazy<ItemConfig> config = Lazy.of(() -> modResourceManager.getItemConfig(getName()));

//    public GeoItemRenderer<AnimatableItem> renderer;

    public AnimatableItem(Properties pProperties) {
        super(pProperties);
    }

//    abstract protected GeoItemRenderer<AnimatableItem> createRenderer();

    public String getName() {
        return name.get();
    }

    @Nullable
    public ItemConfig getConfig() {
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
