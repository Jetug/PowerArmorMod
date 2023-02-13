package com.jetug.power_armor_mod.common.foundation.item;

import com.jetug.power_armor_mod.common.foundation.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.foundation.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.foundation.registery.EntityTypesRegistry;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ArmorFrame extends Item {
    public static final String ENTITY_TAG = "EntityTag";
    public static final String DRAGON_HORN_ENTITY_ID = "DragonHornEntityID";
    public static final String ENTITY_UUID = "EntityUUID";

    public ArmorFrame() {
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

        ItemStack stack = context.getItemInHand();
        var world = context.getLevel();
        context.getClickedPos();
        var entity = new PowerArmorEntity(EntityTypesRegistry.POWER_ARMOR.get(), world);
        entity.setPos(context.getClickLocation());
        world.addFreshEntity(entity);
        stack.shrink(1);
        return InteractionResult.SUCCESS;
    }
}