package com.jetug.power_armor_mod.client.render;

import com.jetug.power_armor_mod.common.util.constants.Global;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import software.bernie.shadowed.eliotlash.mclib.math.functions.limit.Min;

import static com.jetug.power_armor_mod.common.util.constants.Global.*;

public class ModGameRenderer extends GameRenderer {
    private final Minecraft minecraft;

    public ModGameRenderer(Minecraft minecraft, ResourceManager resourceManager, RenderBuffers renderBuffers){
        super(minecraft, resourceManager, renderBuffers);
        this.minecraft = minecraft;
    }

    @Override
    public void pick(float p_109088_) {
        Entity entity = minecraft.getCameraEntity();
        if (entity != null && this.minecraft.level != null) {
            this.minecraft.getProfiler().push("pick");
            this.minecraft.crosshairPickEntity = null;
            double d0 = this.minecraft.gameMode.getPickRange();
            this.minecraft.hitResult = entity.pick(d0, p_109088_, false);
            Vec3 vec3 = entity.getEyePosition(p_109088_);
            boolean flag = false;
            double d1 = d0;
            if (this.minecraft.gameMode.hasFarPickRange()) {
                d1 = 6.0D;
                d0 = d1;
            } else {
                if (d0 > 3.0D) {
                    flag = true;
                }
                d0 = d0;
            }

            d1 *= d1;
            if (this.minecraft.hitResult != null) {
                d1 = this.minecraft.hitResult.getLocation().distanceToSqr(vec3);
            }

            Vec3 vec31 = entity.getViewVector(1.0F);
            Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
            AABB aabb = entity.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);
            EntityHitResult entityhitresult = ProjectileUtil.getEntityHitResult(entity, vec3, vec32, aabb,
                    (p_172770_) -> !p_172770_.isSpectator() && p_172770_.isPickable(), d1);

            if (entityhitresult != null) {
                Entity entity1 = entityhitresult.getEntity();
                Vec3 vec33 = entityhitresult.getLocation();
                double d2 = vec3.distanceToSqr(vec33);
                if (flag && d2 > 9.0D) {
                    this.minecraft.hitResult = BlockHitResult.miss(vec33, Direction.getNearest(vec31.x, vec31.y, vec31.z), new BlockPos(vec33));
                } else if (d2 < d1 || this.minecraft.hitResult == null) {
                    this.minecraft.hitResult = entityhitresult;
                    if (entity1 instanceof LivingEntity || entity1 instanceof ItemFrame) {
                        this.minecraft.crosshairPickEntity = entity1;
                    }
                }
            }

            this.minecraft.getProfiler().pop();
        }
    }
}
