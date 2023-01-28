package com.jetug.power_armor_mod.mixin;

import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorPartEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.isWearingPowerArmor;
import static com.jetug.power_armor_mod.common.util.extensions.PlayerExtension.sendMessage;

@Mixin(ProjectileUtil.class)
public class MixinProjectileUtil {
    private static final String getEntityHitResult = "getEntityHitResult(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;D)Lnet/minecraft/world/phys/EntityHitResult;";

//    @ModifyVariable(method = getEntityHitResult, name = "p_37292_", at = @At(value = "JUMP", ordinal = 0, opcode = Opcodes.IF_ICMPEQ), argsOnly=true)
//    public Predicate<Entity> getEntityHitResult(Predicate<Entity> p_37292_) {
//        var player = Minecraft.getInstance().player;
//        assert player != null;
//        if(isWearingPowerArmor(player)) {
//            try {
//                player.sendMessage(new TextComponent("ModeRenderer"), player.getUUID());
//            } catch (Exception e) {
//            }
//
//            Predicate<Entity> finalFilter = p_37292_;
//            p_37292_ = (levelEntity) -> {
//                var cond1 = !(levelEntity instanceof PowerArmorEntity powerArmor && player.getVehicle() == powerArmor);
//                var cond2 = !(levelEntity instanceof PowerArmorPartEntity powerArmorPart && player.getVehicle() == powerArmorPart.parentMob);
//                return finalFilter.test(levelEntity) && cond1 && cond2;
//            };
//        }
//        return p_37292_;
//    }

    @Inject(at = @At(value = "HEAD"), method = getEntityHitResult, cancellable = true)
    private static void getEntityHitResult(Entity cameraEntity, Vec3 eyePosition, Vec3 pickVector,
                                                      AABB box, Predicate<Entity> filter, double pickRange,
                                                      CallbackInfoReturnable<EntityHitResult> info){
        var player = Minecraft.getInstance().player;
        assert player != null;

        if(isWearingPowerArmor(player))
        {
            //sendMessage("getEntityHitResult");
            var level = cameraEntity.level;
            var _pickRange = pickRange;
            Entity entity = null;
            Vec3 vec3 = null;

            Predicate<Entity> finalFilter = filter;
            filter = (levelEntity) ->{
                var cond1 = !(levelEntity instanceof PowerArmorEntity powerArmor && player.getVehicle() == powerArmor);
                var cond2 = !(levelEntity instanceof PowerArmorPartEntity powerArmorPart && player.getVehicle() == powerArmorPart.parentMob);
                return finalFilter.test(levelEntity) && cond1 && cond2;
            };

            for (Entity levelEntity : level.getEntities(cameraEntity, box, filter)) {
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

            info.setReturnValue(entity == null ? null : new EntityHitResult(entity, vec3));
        }
    }
}
