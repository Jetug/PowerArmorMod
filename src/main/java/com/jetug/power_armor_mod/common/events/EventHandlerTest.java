package com.jetug.power_armor_mod.common.events;

import com.jetug.power_armor_mod.common.capability.data.ArmorDataProvider;
import com.jetug.power_armor_mod.common.capability.data.IArmorPartData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.power_armor_mod.PowerArmorMod.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandlerTest {
    @SubscribeEvent
    public static void entityHurt(LivingHurtEvent event) {
        if(!(event.getSource().getEntity() instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity player = (PlayerEntity) event.getSource().getEntity();

        //Jet
//        Entity entity = event.getEntity();
//        IArmorPartData cap = entity.getCapability(ArmorDataProvider.POWER_ARMOR_PART_DATA).orElse(null);
//        player.sendMessage(new StringTextComponent("Server : " + cap.getDurability()), player.getUUID());
//        cap.setDurability(, cap.getDurability() + 1);
//        //cap.sync((ServerPlayerEntity) player);
//        cap.sync((ServerPlayerEntity)player);
        ///
    }
}
