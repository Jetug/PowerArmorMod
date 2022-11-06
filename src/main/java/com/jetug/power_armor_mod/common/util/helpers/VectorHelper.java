package com.jetug.power_armor_mod.common.util.helpers;

import net.minecraft.util.math.vector.Vector3d;

import static java.lang.Math.*;

public class VectorHelper {

    public static double calculateDistance(Vector3d v1, Vector3d v2){
        return sqrt( pow(v1.x - v2.x, 2) + pow(v1.y - v2.y, 2) + pow(v1.z - v2.z, 2) );
    }

    public static Vector3d getDirection(Vector3d v1, Vector3d v2){
        return new Vector3d(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
    }

    public static Vector3d rotateVector(Vector3d v1, double degrees){

        double angle = degrees;// toRadians(degrees);
        double rotatedX = v1.x * cos(angle) - v1.z * sin(angle);
        double rotatedZ = v1.x * sin(angle) + v1.z * cos(angle);

        return new Vector3d(rotatedX, 0, rotatedZ);
    }
}
