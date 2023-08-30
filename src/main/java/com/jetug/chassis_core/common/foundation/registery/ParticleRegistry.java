package com.jetug.chassis_core.common.foundation.registery;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.foundation.particles.JetParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistry {
    private static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister
            .create(ForgeRegistries.PARTICLE_TYPES, ChassisCore.MOD_ID);

    public static final RegistryObject<SimpleParticleType> JET = PARTICLES.register("jet",
            () -> new SimpleParticleType(true));

    @SubscribeEvent
    public static void registry(ParticleFactoryRegisterEvent event) {
        Minecraft.getInstance().particleEngine.register(JET.get(), JetParticle.Factory::new);
    }

    public static void register(IEventBus eventBus){
        PARTICLES.register(eventBus);
    }
}
