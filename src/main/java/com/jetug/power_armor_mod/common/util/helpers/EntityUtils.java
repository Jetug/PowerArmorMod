package com.jetug.power_armor_mod.common.util.helpers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class EntityUtils {
    public static void push(Entity target, Vec3 vector){
        target.push(vector.x, vector.y, vector.z);
    }
}
