package com.jetug.power_armor_mod.common.minecraft.item;

import com.jetug.power_armor_mod.client.ClientConfig;
import com.jetug.power_armor_mod.client.render.ArmorPartSettings;
import com.jetug.power_armor_mod.common.minecraft.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.util.enums.BodyPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PowerArmorItem extends Item {
    public static final String DAMAGE_KEY = "Damage";
    private ArmorPartSettings armorPartSettings = null;
    private final ModArmorMaterials material;
    public final BodyPart part;

    public PowerArmorItem(ModArmorMaterials material, BodyPart part) {
        super((new Properties()).durability(material.getDurabilityForSlot(part)).tab(ModCreativeModeTab.MY_TAB));
        this.material = material;
        this.part = part;
    }

    public ArmorPartSettings getSettings(){
        if(armorPartSettings == null)
            armorPartSettings = ClientConfig.resourceManager.getPartSettings(part);

        return armorPartSettings;
    }

    public static int getArmorDamage(ItemStack itemStack) {
        if(itemStack.hasTag()) {
            CompoundTag nbt = itemStack.getOrCreateTag();
            return nbt.getInt(DAMAGE_KEY);
        }
        else return 0;
    }

    public static boolean hasArmor(ItemStack itemStack) {
        return getArmorDamage(itemStack) < itemStack.getMaxDamage();
    }

    public void damageArmor(ItemStack itemStack, int dmg)
    {
        var resultDamage = getArmorDamage(itemStack)+dmg;
        setArmorDamage(itemStack, Math.min(resultDamage, itemStack.getMaxDamage()));
    }

    public static void setArmorDamage(ItemStack head, int totalDamage)
    {
        CompoundTag nbt = head.getOrCreateTag();
        nbt.putInt(DAMAGE_KEY, totalDamage);
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
//        player.getItemInHand(hand).hurtAndBreak(1,player, (p) -> p.broadcastBreakEvent(p.getUsedItemHand()));
//        return super.use(level, player, hand);
//    }

//    @Override
//    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
//        context.getItemInHand().hurtAndBreak(1, Objects.requireNonNull(context.getPlayer()),
//                (player) -> player.broadcastBreakEvent(player.getUsedItemHand()));
//
//        return super.onItemUseFirst(stack, context);
//    }
}
