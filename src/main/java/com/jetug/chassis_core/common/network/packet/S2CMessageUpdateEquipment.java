package com.jetug.chassis_core.common.network.packet;

import com.google.common.collect.ImmutableMap;
import com.jetug.chassis_core.common.config.*;
import com.mrcrayfish.framework.api.network.MessageContext;
import com.mrcrayfish.framework.api.network.message.PlayMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.Validate;

public class S2CMessageUpdateEquipment extends PlayMessage<S2CMessageUpdateEquipment> {
    private ImmutableMap<ResourceLocation, Equipment> registeredConfigs;
    private ImmutableMap<ResourceLocation, CustomEquipment> customConfigs;

    public S2CMessageUpdateEquipment() {}

    @Override
    public void encode(S2CMessageUpdateEquipment message, FriendlyByteBuf buffer) {
        Validate.notNull(NetworkEquipmentManager.get());
        Validate.notNull(CustomEquipmentLoader.get());
        NetworkEquipmentManager.get().writeRegisteredConfigs(buffer);
        CustomEquipmentLoader.get().writeCustomGuns(buffer);
    }

    @Override
    public S2CMessageUpdateEquipment decode(FriendlyByteBuf buffer) {
        var message = new S2CMessageUpdateEquipment();
        message.registeredConfigs = NetworkEquipmentManager.readRegisteredConfigs(buffer);
        message.customConfigs = CustomEquipmentLoader.readCustomGuns(buffer);
        return message;
    }

    @Override
    public void handle(S2CMessageUpdateEquipment message, MessageContext supplier) {
        supplier.execute((() -> {
            NetworkEquipmentManager.updateRegisteredConfigs(message);
            CustomEquipmentManager.updateCustomGuns(message);
        }));
        supplier.setHandled(true);
    }

    public ImmutableMap<ResourceLocation, Equipment> getRegisteredConfigs() {
        return this.registeredConfigs;
    }

    public ImmutableMap<ResourceLocation, CustomEquipment> getCustomConfigs() {
        return customConfigs;
    }
}
