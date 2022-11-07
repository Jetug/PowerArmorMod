package com.jetug.power_armor_mod.common.util.helpers;

import net.minecraft.util.math.vector.Vector3d;

import static java.lang.Math.*;

public class VectorHelper {

    public static double CalculateDistance(Vector3d v1, Vector3d v2){
        return sqrt( pow(v1.x - v2.x, 2) + pow(v1.y - v2.y, 2) + pow(v1.z - v2.z, 2) );
    }

    public static Vector3d GetDirection(Vector3d v1, Vector3d v2){
        return new Vector3d(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }
}
