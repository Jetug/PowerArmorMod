package com.jetug.power_armor_mod.client.render;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class ModGameRenderer extends GameRenderer {
    private final Minecraft minecraft;

    public ModGameRenderer(Minecraft minecraft, ResourceManager resourceManager, RenderBuffers renderBuffers){
        super(minecraft, resourceManager, renderBuffers);
        this.minecraft = minecraft;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    public void pick(float p_109088_) {
        Entity cameraEntity = minecraft.getCameraEntity();
        if (cameraEntity != null && this.minecraft.level != null) {
            this.minecraft.getProfiler().push("pick");
            this.minecraft.crosshairPickEntity = null;
            double d0 = this.minecraft.gameMode.getPickRange();
            this.minecraft.hitResult = cameraEntity.pick(d0, p_109088_, false);
            Vec3 vec3 = cameraEntity.getEyePosition(p_109088_);
            boolean flag = false;
            double d1 = d0;

            if (this.minecraft.gameMode.hasFarPickRange()) {
                d1 = 6.0D;
                d0 = d1;
            } else if (d0 > 3.0D) {
                flag = true;
            }

            d1 *= d1;

            if (this.minecraft.hitResult != null) {
                d1 = this.minecraft.hitResult.getLocation().distanceToSqr(vec3);
            }

            Vec3 vec31 = cameraEntity.getViewVector(1.0F);
            Vec3 vec32 = vec3.add(vec31.x * d0, vec31.y * d0, vec31.z * d0);
            AABB aabb = cameraEntity.getBoundingBox().expandTowards(vec31.scale(d0)).inflate(1.0D, 1.0D, 1.0D);

            EntityHitResult entityHitResult = getEntityHitResult(
                    cameraEntity, vec3, vec32, aabb,
                    (p_172770_) -> !p_172770_.isSpectator() && p_172770_.isPickable(), d1);

            if (entityHitResult != null) {
                var hitEntity = entityHitResult.getEntity();
                var vec33 = entityHitResult.getLocation();
                var d2 = vec3.distanceToSqr(vec33);

                if (flag && d2 > 9.0D) {
                    this.minecraft.hitResult = BlockHitResult.miss(vec33,
                            Direction.getNearest(vec31.x, vec31.y, vec31.z), new BlockPos(vec33));
                } else if (d2 < d1 || this.minecraft.hitResult == null) {
                    this.minecraft.hitResult = entityHitResult;
                    if (hitEntity instanceof LivingEntity || hitEntity instanceof ItemFrame) {
                        this.minecraft.crosshairPickEntity = hitEntity;
                    }
                }
            }

            this.minecraft.getProfiler().pop();
        }
    }

    @Nullable
    private static EntityHitResult getEntityHitResult(Entity cameraEntity, Vec3 vector1, Vec3 vector2,
                                                     AABB box, Predicate<Entity> onGetEntity, double d) {
        var level = cameraEntity.level;
        var d0 = d;
        Entity entity = null;
        Vec3 vec3 = null;

        for(Entity levelEntity : level.getEntities(cameraEntity, box, onGetEntity)) {
            var aabb = levelEntity.getBoundingBox().inflate(levelEntity.getPickRadius());
            var optional = aabb.clip(vector1, vector2);

            if (aabb.contains(vector1)) {
                if (d0 >= 0.0D) {
                    entity = levelEntity;
                    vec3 = optional.orElse(vector1);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = vector1.distanceToSqr(vec31);

                if (d1 < d0 || d0 == 0.0D) {
                    if (levelEntity.getRootVehicle() instanceof PowerArmorEntity){
                    //if (levelEntity.getRootVehicle() == cameraEntity.getRootVehicle() && !levelEntity.canRiderInteract()) {

                        var t1 = levelEntity.getPassengers();
                        var tt1 = levelEntity.getVehicle();
                        var t2 = cameraEntity.getPassengers();
                        var tt2 = cameraEntity.getVehicle();

                        Global.LOGGER.info("PICK");
                        if (d0 == 0.0D) {
                            entity = levelEntity;
                            vec3 = vec31;
                        }
                    } else {
                        entity = levelEntity;
                        vec3 = vec31;
                        d0 = d1;
                    }
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, vec3);
    }
}
