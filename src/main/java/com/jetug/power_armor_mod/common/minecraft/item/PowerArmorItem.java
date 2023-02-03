package com.jetug.power_armor_mod.common.minecraft.item;

import com.jetug.power_armor_mod.client.render.ArmorPartSettings;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class PowerArmorItem extends Item {
    public static final String DAMAGE_KEY = "Damage";
    //private final ArmorPartSettings armorPartSettings = null;

    public final BodyPart part;

    public PowerArmorItem(BodyPart part, Properties properties) {
        super(properties);
        this.part = part;
    }

    public int getArmorDamage(ItemStack itemStack)
    {
        if(itemStack.hasTag())
        {
            CompoundTag nbt = itemStack.getOrCreateTag();
            return nbt.getInt(DAMAGE_KEY);
        }
        else
            return 0;
    }

    public void damageArmor(ItemStack itemStack, int dmg)
    {
        setArmorDamage(itemStack, getArmorDamage(itemStack)+dmg);
    }

    public static void setArmorDamage(ItemStack head, int totalDamage)
    {
        CompoundTag nbt = head.getOrCreateTag();
        nbt.putInt(DAMAGE_KEY, totalDamage);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        player.getItemInHand(hand).hurtAndBreak(1,player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        context.getItemInHand().hurtAndBreak(1, Objects.requireNonNull(context.getPlayer()),
                (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return super.onItemUseFirst(stack, context);
    }
}
