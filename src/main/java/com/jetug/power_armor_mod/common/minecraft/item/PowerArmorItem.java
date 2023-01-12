package com.jetug.power_armor_mod.common.minecraft.item;

import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class PowerArmorItem extends Item {
    public final BodyPart part;

    public PowerArmorItem(BodyPart part, Properties properties) {
        super(properties);
        this.part = part;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        context.getItemInHand().hurtAndBreak(1, context.getPlayer(),
                (player) -> player.broadcastBreakEvent(player.getUsedItemHand())
        );

        return super.onItemUseFirst(stack, context);
    }
}
