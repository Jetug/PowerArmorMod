package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.data.enums.BodyPart;
import com.jetug.power_armor_mod.common.data.json.EquipmentSettings;
import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.util.helpers.ResourceHelper;
import net.minecraft.world.item.Item;

import javax.annotation.Nullable;

import static com.jetug.power_armor_mod.client.ClientConfig.modResourceManager;

public class CastItem extends Item {
    private final FrameArmorItem result;

    public CastItem(FrameArmorItem result) {
        super(new Properties().tab(ModCreativeModeTab.MY_TAB).stacksTo(1));
        this.result = result;
    }

    public FrameArmorItem getResult() {
        return result;
    }
}
