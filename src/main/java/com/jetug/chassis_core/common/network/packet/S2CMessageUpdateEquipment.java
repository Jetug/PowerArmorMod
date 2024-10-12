package com.jetug.chassis_core.common.network.packet;

import com.google.common.collect.ImmutableMap;
import com.jetug.chassis_core.common.config.Equipment;
import com.jetug.chassis_core.common.config.NetworkEquipmentManager;
import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;

public class S2CMessageUpdateEquipment extends PlayMessage<S2CMessageUpdateEquipment> {
    private ImmutableMap<ResourceLocation, Equipment> registeredConfigs;

    public S2CMessageUpdateEquipment() {}

    @Override
    public void encode(S2CMessageUpdateEquipment message, FriendlyByteBuf buffer) {
        Validate.notNull(NetworkEquipmentManager.get());
        NetworkEquipmentManager.get().writeRegisteredConfigs(buffer);
    }

    @Override
    public S2CMessageUpdateEquipment decode(FriendlyByteBuf buffer) {
        S2CMessageUpdateEquipment message = new S2CMessageUpdateEquipment();
        message.registeredConfigs = NetworkEquipmentManager.readRegisteredConfigs(buffer);
        return message;
    }

    @Override
    public void handle(S2CMessageUpdateEquipment message, MessageContext supplier) {
        supplier.execute((() -> {
            NetworkEquipmentManager.updateRegisteredConfigs(message);
        }));
        supplier.setHandled(true);
    }

    public ImmutableMap<ResourceLocation, Equipment> getRegisteredConfigs() {
        return this.registeredConfigs;
    }
}
