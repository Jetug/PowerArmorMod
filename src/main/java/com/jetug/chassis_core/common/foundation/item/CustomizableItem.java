package com.jetug.chassis_core.common.foundation.item;

import com.jetug.chassis_core.common.data.json.ItemConfig;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

import static com.jetug.chassis_core.client.ClientConfig.modResourceManager;
import static com.jetug.chassis_core.client.render.utils.ResourceHelper.getResourceName;

public abstract class CustomizableItem extends Item implements IConfigProvider {
    private final Lazy<String> name = Lazy.of(() -> getResourceName(ForgeRegistries.ITEMS.getKey(this)));
    private final Lazy<ItemConfig> config = Lazy.of(() -> modResourceManager.getItemConfig(getName()));

    public CustomizableItem(Properties pProperties) {
        super(pProperties);
    }

    public String getName() {
        return name.get();
    }

    @Nullable
    public ItemConfig getConfig() {
        return config.get();
    }
}
