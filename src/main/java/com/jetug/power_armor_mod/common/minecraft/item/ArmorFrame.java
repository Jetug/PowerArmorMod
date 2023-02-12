package com.jetug.power_armor_mod.common.minecraft.item;

import com.jetug.power_armor_mod.common.minecraft.ModCreativeModeTab;
import com.jetug.power_armor_mod.common.minecraft.entity.PowerArmorEntity;
import com.jetug.power_armor_mod.common.minecraft.registery.EntityTypesRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

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