package com.jetug.begining.client.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.jetug.begining.common.util.extensions.PlayerExtension.isWearingPowerArmor;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KAIMyEntityRegisterClient
{
//    public static PlayerPowerArmorRenderer renderer = new PlayerPowerArmorRenderer()
//
//    @SubscribeEvent
//    @OnlyIn(Dist.CLIENT)
//    public void onRenderLiving(RenderLivingEvent.Pre event) {
//        if (event.getEntity() instanceof PlayerEntity) {
//            event.setCanceled(true);
//
//            final PlayerPowerArmorRenderer modelRenderer = new PlayerPowerArmorRenderer();
//            modelRenderer.render(event.getEntity(), event.getEntity().yRot, 0, event.getMatrixStack(), event.getBuffers(), (int)event.getPartialRenderTick()); // render the new renderer
//        }
//    }
//
//    //Jet

//    public static TestRenderer testRenderer = new TestRenderer(Minecraft.getInstance().getEntityRenderDispatcher());
//    PowerArmorRenderer powerArmorRenderer = new PowerArmorRenderer(Minecraft.getInstance().getEntityRenderDispatcher());


//    @OnlyIn(Dist.CLIENT)
//    @SubscribeEvent
//    public static void onRenderEntity(RenderLivingEvent event)
//    {
//        //if (event.getEntity() instanceof PowerArmorEntity /*&& event.getEntity().isVehicle()*/)
//            //event.setCanceled(true);
//    }
//
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        PlayerEntity player = event.getPlayer();

        //ClientPlayerEntity clientPlayer = Minecraft.getInstance().player;
        if(isWearingPowerArmor(player)){
            player.setInvisible(true);
        }
//        TestRenderer testRenderer = new TestRenderer(Minecraft.getInstance().getEntityRenderDispatcher());
//        if (event.getEntity() == null)
//            return;
//
//        testRenderer.render(event.getPlayer(), 0, 0, event.getMatrixStack(), event.getBuffers(), 0);
//
//        event.setCanceled(true);

        //RenderingRegistry.registerEntityRenderingHandler(EntityType.PLAYER, TestRenderer::new);
    }
}
