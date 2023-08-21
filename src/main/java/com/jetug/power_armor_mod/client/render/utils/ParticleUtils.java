package com.jetug.power_armor_mod.client.render.utils;

import com.jetug.power_armor_mod.common.foundation.particles.Pos3D;
import com.mojang.math.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;

import java.util.Random;

public class ParticleUtils {
    public static void showJetpackParticles(Vector3d worldPos) {
        var minecraft = Minecraft.getInstance();
        var particle = ParticleTypes.SMOKE;
        var rand = new Random();
        var random = (rand.nextFloat() - 0.5F) * 0.1F;
        var pos3D = new Pos3D(worldPos);
        var vec = pos3D.translate(0, 0, 0).translate(new Pos3D(minecraft.player.getDeltaMovement()));
        for(var i = 0; i < 3; i++){
            minecraft.level.addParticle(particle, vec.x, vec.y, vec.z, random, -0.2D, random);
        }
    }
}
