package com.jetug.chassis_core.common.data.json;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

import static com.jetug.chassis_core.common.util.helpers.TextureHelper.createResource;
import static com.jetug.chassis_core.common.util.helpers.TextureHelper.cropTexture;

public class ItemConfig extends ModelConfigBase {
    public String parent;
    public String[] mods = new String[0];
}