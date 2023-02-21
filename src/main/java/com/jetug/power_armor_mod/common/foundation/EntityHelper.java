package com.jetug.power_armor_mod.common.foundation;

import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.registery.ItemRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EntityHelper {
    public static final String ENTITY_TAG = "EntityTag";
    public static final String ITEM_ENTITY_ID = "ItemEntityID";
    public static final String ENTITY_UUID = "EntityUUID";

    public static InteractionResult giveEntityItemToPlayer(Player playerIn, LivingEntity target, InteractionHand hand) {
        if (!playerIn.level.isClientSide && hand == InteractionHand.MAIN_HAND) {

            playerIn.swing(hand);
            playerIn.level.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 3.0F, 0.75F);
            var entityInItem = entityToItem(target);
            playerIn.getInventory().add(entityInItem);

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    public static @NotNull ItemStack entityToItem(LivingEntity target) {
        var trueStack = new ItemStack(ItemRegistry.PA_FRAME.get());
        var newTag    = new CompoundTag();
        var entityTag = new CompoundTag();

        target.save(entityTag);
        newTag.put(ENTITY_TAG, entityTag);

        newTag.putString(ITEM_ENTITY_ID, Registry.ENTITY_TYPE.getKey(target.getType()).toString());
        trueStack.setTag(newTag);
        target.remove(Entity.RemovalReason.DISCARDED);
        return trueStack;
    }

    @Nullable
    public static Entity entityFromItem(ItemStack stack, Level world) {
        if (stack.getTag() != null && !stack.getTag().getString(ITEM_ENTITY_ID).isEmpty()) {
            var id = stack.getTag().getString(ITEM_ENTITY_ID);
            var type = EntityType.byString(id).orElse(null);
            if (type != null) {
                Entity entity = type.create(world);

                if(entity == null) return null;

                entity.load(stack.getTag().getCompound(ENTITY_TAG));

                if (stack.getTag().contains(ENTITY_UUID))
                    entity.setUUID(stack.getTag().getUUID(ENTITY_UUID));
                return entity;

            }
        }
        return null;
    }

    @SuppressWarnings("DataFlowIssue")
    public static void clearItemTags(ItemStack stack){
        CompoundTag tag = stack.getTag();
        tag.remove(ITEM_ENTITY_ID);
        tag.remove(ENTITY_TAG);
        tag.remove(ENTITY_UUID);
        stack.setTag(tag);
    }
}
