package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.data.constants.Global;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.registery.EntityTypeRegistry;
import com.mojang.math.Vector3d;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.EventPriority;

import static java.lang.System.out;

@Mod.EventBusSubscriber(modid = Global.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CustomHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onEntityContainerChange(ContainerChangedEvent event) {
        var entity = event.getEntity();
        out.println(entity);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        var world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        Direction direction = event.getPlayer().getDirection();
        BlockPos centerPos = pos.offset(direction.getNormal());

        // Копаем блоки 3 на 3
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                BlockPos targetPos = centerPos.offset(direction.getStepX() * xOffset, yOffset, direction.getStepZ() * xOffset);
                BlockState targetState = world.getBlockState(targetPos);

                world.destroyBlock(targetPos, true);
            }
        }
    }

    public static BlockPos getBlockLookingAt() {
        Minecraft minecraft = Minecraft.getInstance();

        var playerPos = minecraft.player.getPosition(1f);
        var lookVec = minecraft.player.getViewVector(1f);
        double reachDistance = 5.0;

//        BucketItem
//
//        Vector3d endVec = playerPos.add(lookVec.scale(reachDistance));
//        RayTraceResult result = minecraft.level.blocj.rayTraceBlocks(new RayTraceContext(playerPos, endVec, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, minecraft.player));
//
//        if (result.getType() == RayTraceResult.Type.BLOCK) {
//            return result.getPos();
//        }

        return null;
    }


//    @SubscribeEvent
//    public static void onPlayerInteractEntity(PlayerInteractEvent.EntityInteract event) {
//        Player player = event.getPlayer();
//        Entity entity = event.getTarget();
//
//        var t = entity;
//        // Ваш код для работы с сущностью, на которую игрок смотрит
//    }

}