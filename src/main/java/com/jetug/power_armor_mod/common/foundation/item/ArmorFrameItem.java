package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.registery.EntityTypeRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.jetug.power_armor_mod.common.foundation.EntityHelper.*;

public class ArmorFrameItem extends Item {
    public ArmorFrameItem() {
        super((new Item.Properties()).stacksTo(1).tab(ModCreativeModeTab.MY_TAB));
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP)
            return InteractionResult.FAIL;

        var savedEntity = entityFromItem(context.getItemInHand(), context.getLevel());

        if(savedEntity != null)
            summonSavedEntity(savedEntity, context);
        else summonNewEntity(context);

        return InteractionResult.SUCCESS;
    }

    private static void summonSavedEntity(Entity savedEntity, UseOnContext context) {
        var stack = context.getItemInHand();

        savedEntity.absMoveTo(context.getClickedPos().getX() + 0.5D,
                (context.getClickedPos().getY() + 1),
                context.getClickedPos().getZ() + 0.5D,
                180 + (context.getHorizontalDirection()).toYRot(), 0.0F);

        if (context.getLevel().addFreshEntity(savedEntity)) {
            clearItemTags(stack);
            stack.shrink(1);
        }
    }

    private static void summonNewEntity(UseOnContext context) {
        var stack = context.getItemInHand();
        var world = context.getLevel();

        context.getClickedPos();
        var entity = new PowerArmorEntity(EntityTypeRegistry.POWER_ARMOR.get(), world);
        entity.setPos(context.getClickLocation());

        if (world.addFreshEntity(entity)) {
            stack.shrink(1);
        }
    }
}