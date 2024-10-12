package com.jetug.chassis_core.common.config.holders;

import com.jetug.chassis_core.ChassisCore;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class BodyPart extends ResourceHolder {
    public static final BodyPart HEAD         = new BodyPart("head"       );
    public static final BodyPart BODY         = new BodyPart("body"       );
    public static final BodyPart LEFT_ARM     = new BodyPart("left_arm"   );
    public static final BodyPart RIGHT_ARM    = new BodyPart("right_arm"  );
    public static final BodyPart LEFT_LEG     = new BodyPart("left_leg"   );
    public static final BodyPart RIGHT_LEG    = new BodyPart("right_leg"  );

    private static final Map<ResourceLocation, BodyPart> fireModeMap = new HashMap<>();

    static {
        registerType(HEAD     );
        registerType(BODY     );
        registerType(LEFT_ARM );
        registerType(RIGHT_ARM);
        registerType(LEFT_LEG );
        registerType(RIGHT_LEG);
    }

    public BodyPart(ResourceLocation id) {
        super(id);
    }

    public BodyPart(String id) {
        this(new ResourceLocation(ChassisCore.MOD_ID, id));
    }

//    public ResourceLocation getIcon() {
//        return new ResourceLocation(id.getNamespace(), "textures/hud/body_part/" + id.getPath() + ".png");
//    }

    public static void registerType(BodyPart mode) {
        fireModeMap.putIfAbsent(mode.getId(), mode);
    }

    public static BodyPart getPart(ResourceLocation id) {
        return fireModeMap.getOrDefault(id, HEAD);
    }

    public static BodyPart getPart(String id) {
        return getPart(ResourceLocation.tryParse(id));
    }
}
