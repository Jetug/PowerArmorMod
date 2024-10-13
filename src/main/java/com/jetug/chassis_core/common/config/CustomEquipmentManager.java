package com.jetug.chassis_core.common.config;

import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.network.packet.S2CMessageUpdateEquipment;
import com.mrcrayfish.framework.api.data.login.ILoginData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.Optional;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID, value = Dist.CLIENT)
public class CustomEquipmentManager {
    private static Map<ResourceLocation, CustomEquipment> customGunMap;

    public static boolean updateCustomGuns(S2CMessageUpdateEquipment message) {
        return updateCustomGuns(message.getCustomConfigs());
    }

    private static boolean updateCustomGuns(Map<ResourceLocation, CustomEquipment> customGunMap) {
        CustomEquipmentManager.customGunMap = customGunMap;
        return true;
    }

    @SubscribeEvent
    public static void onClientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        customGunMap = null;
    }

    public static class LoginData implements ILoginData {
        @Override
        public void writeData(FriendlyByteBuf buffer) {
            Validate.notNull(CustomEquipmentLoader.get());
            CustomEquipmentLoader.get().writeCustomGuns(buffer);
        }

        @Override
        public Optional<String> readData(FriendlyByteBuf buffer) {
            Map<ResourceLocation, CustomEquipment> customGuns = CustomEquipmentLoader.readCustomGuns(buffer);
            CustomEquipmentManager.updateCustomGuns(customGuns);
            return Optional.empty();
        }
    }
}
