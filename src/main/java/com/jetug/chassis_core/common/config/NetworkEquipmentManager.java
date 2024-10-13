package com.jetug.chassis_core.common.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.jetug.chassis_core.ChassisCore;
import com.jetug.chassis_core.common.foundation.item.ChassisEquipment;
import com.jetug.chassis_core.common.network.FrameworkPacketHandler;
import com.jetug.chassis_core.common.network.packet.S2CMessageUpdateEquipment;
import com.mrcrayfish.framework.api.data.login.ILoginData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.util.*;

import static net.minecraftforge.registries.ForgeRegistries.ITEMS;

/**
 * Author: MrCrayfish
 */
@Mod.EventBusSubscriber(modid = ChassisCore.MOD_ID)
public class NetworkEquipmentManager extends NetworkManager<ChassisEquipment, Equipment> {
    private static List<ChassisEquipment> clientRegisteredConfigs = new ArrayList<>();
    private static NetworkEquipmentManager instance;

    private Map<ResourceLocation, Equipment> registeredConfigs = new HashMap<>();

    @Override
    protected Boolean check(Item v) {
        return v instanceof ChassisEquipment;
    }

    @Override
    protected Class<Equipment> getConfigClass() {
        return Equipment.class;
    }

    @Override
    protected String getPath() {
        return "config";
    }

    /**
     * Writes all registered Configs into the provided packet buffer
     *
     * @param buffer a packet buffer get
     */
    public void writeRegisteredConfigs(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.registeredConfigs.size());
        this.registeredConfigs.forEach((id, equipment) -> {
            buffer.writeResourceLocation(id);
            buffer.writeNbt(equipment.serializeNBT());
        });
    }

    /**
     * Reads all registered guns from the provided packet buffer
     *
     * @param buffer a packet buffer get
     * @return a map of registered guns from the server
     */
    public static ImmutableMap<ResourceLocation, Equipment> readRegisteredConfigs(FriendlyByteBuf buffer) {
        var size = buffer.readVarInt();

        if (size > 0) {
            ImmutableMap.Builder<ResourceLocation, Equipment> builder = ImmutableMap.builder();

            for (int i = 0; i < size; i++) {
                var id = buffer.readResourceLocation();
                var equipment = Equipment.create(id, buffer.readNbt());
                builder.put(id, equipment);
            }
            return builder.build();
        }
        return ImmutableMap.of();
    }

    public static boolean updateRegisteredConfigs(S2CMessageUpdateEquipment message) {
        return updateRegisteredConfigs(message.getRegisteredConfigs());
    }

    /**
     * Updates registered guns from data provided by the server
     *
     * @return true if all registered guns were able to update their corresponding config item
     */
    private static boolean updateRegisteredConfigs(Map<ResourceLocation, Equipment> registeredConfigs) {
        clientRegisteredConfigs.clear();
        if (registeredConfigs != null) {
            for (Map.Entry<ResourceLocation, Equipment> entry : registeredConfigs.entrySet()) {
                Item item = ITEMS.getValue(entry.getKey());
                if (!(item instanceof ChassisEquipment)) {
                    return false;
                }
                ((ChassisEquipment) item).setConfig(new Supplier<>(entry.getValue()));
                clientRegisteredConfigs.add((ChassisEquipment) item);
            }
            return true;
        }
        return false;
    }

    /**
     * Gets a map of all the registered guns objects. Note, this is an immutable map.
     *
     * @return a map of registered config objects
     */
    public Map<ResourceLocation, Equipment> getRegisteredConfigs() {
        return this.registeredConfigs;
    }

    /**
     * Gets a list of all the guns registered on the client side. Note, this is an immutable list.
     *
     * @return a map of guns registered on the client
     */
    public static List<ChassisEquipment> getClientRegisteredConfigs() {
        return ImmutableList.copyOf(clientRegisteredConfigs);
    }

    @SubscribeEvent
    public static void onServerStopped(ServerStoppedEvent event) {
        NetworkEquipmentManager.instance = null;
    }

    @SubscribeEvent
    public static void addReloadListenerEvent(AddReloadListenerEvent event) {
        NetworkEquipmentManager networkEquipmentManager = new NetworkEquipmentManager();
        event.addListener(networkEquipmentManager);
        NetworkEquipmentManager.instance = networkEquipmentManager;
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            FrameworkPacketHandler.getPlayChannel().sendToAll(new S2CMessageUpdateEquipment());
        }
    }

    /**
     * Gets the network config manager. This will be null if the client isn't running an integrated
     * server or the client is connected to a dedicated server.
     *
     * @return the network config manager
     */
    @Nullable
    public static NetworkEquipmentManager get() {
        return instance;
    }

    public static class LoginData implements ILoginData {
        @Override
        public void writeData(FriendlyByteBuf buffer) {
            Validate.notNull(NetworkEquipmentManager.get());
            NetworkEquipmentManager.get().writeRegisteredConfigs(buffer);
        }

        @Override
        public Optional<String> readData(FriendlyByteBuf buffer) {
            var registeredConfigs = NetworkEquipmentManager.readRegisteredConfigs(buffer);
            NetworkEquipmentManager.updateRegisteredConfigs(registeredConfigs);
            return Optional.empty();
        }
    }
}
