package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.client.render.utils.ResourceHelper;
import com.jetug.chassis_core.common.data.json.EquipmentConfig;
import mod.azure.azurelib.animatable.GeoItem;
import mod.azure.azurelib.core.animatable.instance.AnimatableInstanceCache;
import mod.azure.azurelib.core.animation.AnimatableManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;

import static com.jetug.chassis_core.client.ClientConfig.*;
import static com.jetug.chassis_core.common.foundation.item.StackUtils.*;
import static mod.azure.azurelib.util.AzureLibUtil.*;

public class ChassisEquipment extends Item implements GeoItem {
    public final String part;
    private final AnimatableInstanceCache cache = createInstanceCache(this);
    private final Lazy<String> name = Lazy.of(() -> ResourceHelper.getResourceName(getRegistryName()));
    private final Lazy<EquipmentConfig> config = Lazy.of(() -> modResourceManager.getEquipmentConfig(getName()));

    public ChassisEquipment(Properties pProperties, String part) {
        super(pProperties);
        this.part = part;
    }

    @Nullable
    public ResourceLocation getTexture(ItemStack stack){
        var variant = getVariant(stack);
        var value = getConfig().getTextureLocation(variant);
        return value != null ? value : getConfig().getTextureLocation(DEFAULT);
    }

    @Nullable
    public EquipmentConfig getConfig(){
        return config.get();
    }

    public String getName(){
        return name.get();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}
}
