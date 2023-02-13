package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import net.minecraft.core.Direction;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ArmorFrameOrig extends Item {
    public static final String ENTITY_TAG = "EntityTag";
    public static final String DRAGON_HORN_ENTITY_ID = "DragonHornEntityID";
    public static final String ENTITY_UUID = "EntityUUID";

    public ArmorFrameOrig() {
        super((new Properties()).stacksTo(1).tab(ModCreativeModeTab.MY_TAB));
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }


    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player playerIn, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        ItemStack trueStack = playerIn.getItemInHand(hand);
        if (!playerIn.level.isClientSide
                && hand == InteractionHand.MAIN_HAND
                && target instanceof PowerArmorEntity
                && (trueStack.getTag() == null
                || trueStack.getTag().getCompound(ENTITY_TAG).isEmpty())) {
            CompoundTag newTag = new CompoundTag();

            CompoundTag entityTag = new CompoundTag();
            target.save(entityTag);
            newTag.put(ENTITY_TAG, entityTag);

            newTag.putString(DRAGON_HORN_ENTITY_ID, Registry.ENTITY_TYPE.getKey(target.getType()).toString());
            trueStack.setTag(newTag);

            playerIn.swing(hand);
            playerIn.level.playSound(playerIn, playerIn.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 3.0F, 0.75F);
            target.remove(Entity.RemovalReason.DISCARDED);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP)
            return InteractionResult.FAIL;

        ItemStack stack = context.getItemInHand();

        if (stack.getTag() != null && !stack.getTag().getString(DRAGON_HORN_ENTITY_ID).isEmpty()) {
            var world = context.getLevel();
            var id = stack.getTag().getString(DRAGON_HORN_ENTITY_ID);
            var type = EntityType.byString(id).orElse(null);

            //world.addFreshEntity(new PowerArmorEntity(EntityTypesRegistry.POWER_ARMOR.get(), world));

            if (type != null) {
                Entity entity = type.create(world);

                if (entity instanceof PowerArmorEntity dragon) {
                    dragon.load(stack.getTag().getCompound(ENTITY_TAG));
                }

                if (stack.getTag().contains(ENTITY_UUID))
                    entity.setUUID(stack.getTag().getUUID(ENTITY_UUID));

                entity.absMoveTo(context.getClickedPos().getX() + 0.5D,
                        (context.getClickedPos().getY() + 1),
                        context.getClickedPos().getZ() + 0.5D,
                        180 + (context.getHorizontalDirection()).toYRot(), 0.0F);

                if (world.addFreshEntity(entity)) {
                    CompoundTag tag = stack.getTag();
                    tag.remove(DRAGON_HORN_ENTITY_ID);
                    tag.remove(ENTITY_TAG);
                    tag.remove(ENTITY_UUID);
                    stack.setTag(tag);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}