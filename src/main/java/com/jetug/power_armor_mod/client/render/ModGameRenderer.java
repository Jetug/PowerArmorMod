package com.jetug.power_armor_mod.client.render;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorPartEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Predicate;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;

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
            double pickRange = this.minecraft.gameMode.getPickRange();
            this.minecraft.hitResult = cameraEntity.pick(pickRange, p_109088_, false);
            Vec3 eyePosition = cameraEntity.getEyePosition(p_109088_);
            boolean flag = false;
            double finalPickRange = pickRange;

            if (this.minecraft.gameMode.hasFarPickRange()) {
                finalPickRange = 6.0D;
                pickRange = finalPickRange;
            } else if (pickRange > 3.0D) {
                flag = true;
            }

            finalPickRange *= finalPickRange;

            if (this.minecraft.hitResult != null) {
                finalPickRange = this.minecraft.hitResult.getLocation().distanceToSqr(eyePosition);
            }

            Vec3 viewVector = cameraEntity.getViewVector(1.0F);
            Vec3 pickVector = eyePosition.add(viewVector.x * pickRange, viewVector.y * pickRange, viewVector.z * pickRange);
            AABB aabb = cameraEntity.getBoundingBox().expandTowards(viewVector.scale(pickRange)).inflate(1.0D, 1.0D, 1.0D);

            EntityHitResult entityHitResult = getEntityHitResult(
                    cameraEntity, eyePosition, pickVector, aabb,
                    (entity) -> !entity.isSpectator() && entity.isPickable(), finalPickRange);

            if (entityHitResult != null) {
                Entity hitEntity = entityHitResult.getEntity();
                Vec3 hitEntityLocation = entityHitResult.getLocation();
                double distanceToHitEntity = eyePosition.distanceToSqr(hitEntityLocation);

                if (flag && distanceToHitEntity > 9.0D) {
                    this.minecraft.hitResult = BlockHitResult.miss(hitEntityLocation,
                            Direction.getNearest(viewVector.x, viewVector.y, viewVector.z),
                            new BlockPos(hitEntityLocation));

                } else if (distanceToHitEntity < finalPickRange || this.minecraft.hitResult == null) {
                    this.minecraft.hitResult = entityHitResult;
                    if (hitEntity instanceof LivingEntity || hitEntity instanceof ItemFrame) {
                        this.minecraft.crosshairPickEntity = hitEntity;
                    }
                }
            }

            this.minecraft.getProfiler().pop();
        }
    }

//                var distance = eyePosition.distanceToSqr(levelEntity.getEyePosition());
//                var aabb = levelEntity.getBoundingBox().inflate(levelEntity.getPickRadius());
//
//                Vec3 viewVector = cameraEntity.getViewVector(1.0F);
//                Vec3 pickVector = eyePosition.add(viewVector.x * pickRange, viewVector.y * pickRange, viewVector.z * pickRange);
//                var optional = aabb.clip(eyePosition, pickVector);

//                if(distance < minDistance){
//                    minDistance = distance;
//                    entity = levelEntity;
//                    vec3 = optional.orElse(eyePosition);
//                }

    @Nullable
    private static EntityHitResult getEntityHitResult(Entity cameraEntity, Vec3 eyePosition, Vec3 pickVector,
                                                      AABB box, Predicate<Entity> filter, double pickRange) {
        Level level = cameraEntity.level;
        double _pickRange = pickRange;
        Entity entity = null;
        Vec3 vec3 = null;

        var player = Minecraft.getInstance().player;

        for(Entity levelEntity : level.getEntities(cameraEntity, box, filter)) {
            if(levelEntity instanceof PowerArmorEntity && player != null && player.getVehicle() == levelEntity)
                continue;
            if(levelEntity instanceof PowerArmorPartEntity && player != null && player.getVehicle() == ((PowerArmorPartEntity)levelEntity).parentMob)
                continue;

            AABB aabb = levelEntity.getBoundingBox().inflate(levelEntity.getPickRadius());
            Optional<Vec3> optional = aabb.clip(eyePosition, pickVector);

            if (aabb.contains(eyePosition)) {
                if (_pickRange >= 0.0D) {
                    entity = levelEntity;
                    vec3 = optional.orElse(eyePosition);
                    _pickRange = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 optionalVector = optional.get();
                double distance = eyePosition.distanceToSqr(optionalVector);

                if (distance < _pickRange || _pickRange == 0.0D) {
                    if (levelEntity.getRootVehicle() == cameraEntity.getRootVehicle() && !levelEntity.canRiderInteract()) {
                        if (_pickRange == 0.0D) {
                            entity = levelEntity;
                            vec3 = optionalVector;
                        }
                    } else {
                        entity = levelEntity;
                        vec3 = optionalVector;
                        _pickRange = distance;
                    }
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, vec3);
    }

//    @Nullable
//    private static EntityHitResult getEntityHitResult(Entity cameraEntity, Vec3 eyePosition, Vec3 vector2,
//                                                      AABB box, Predicate<Entity> filter, double pickRange1) {
//        var level = cameraEntity.level;
//        double _pickRange = pickRange1;
//        Entity entity = null;
//        Vec3 vec3 = null;
//
//        for(Entity levelEntity : level.getEntities(cameraEntity, box, filter)) {
//            var aabb = levelEntity.getBoundingBox().inflate(levelEntity.getPickRadius());
//            var optional = aabb.clip(eyePosition, vector2);
//
//            if (aabb.contains(eyePosition)) {
//                if (_pickRange >= 0.0D) {
//                    entity = levelEntity;
//                    vec3 = optional.orElse(eyePosition);
//                    _pickRange = 0.0D;
//                }
//            } else if (optional.isPresent()) {
//                var optionalVector = optional.get();
//                var distance = eyePosition.distanceToSqr(optionalVector);
//
//                if (distance < _pickRange || _pickRange == 0.0D) {
//                    if (levelEntity.getRootVehicle() == cameraEntity.getRootVehicle() && !levelEntity.canRiderInteract()) {
//                        if (_pickRange == 0.0D) {
//                            entity = levelEntity;
//                            vec3 = optionalVector;
//                        }
//                    } else {
//                        entity = levelEntity;
//                        vec3 = optionalVector;
//                        _pickRange = distance;
//                    }
//                }
//            }
//        }
//
//        if(entity instanceof PowerArmorEntity && entity.getControllingPassenger() == Minecraft.getInstance().player)
//            return new EntityHitResult(entity, vector2);
//        else
//            return entity == null ? null : new EntityHitResult(entity, vec3);
//    }
}
