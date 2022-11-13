package com.jetug.power_armor_mod.common.network;

import com.jetug.power_armor_mod.common.network.packet.ArmorClientUpdatePacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartClientPacket;
import com.jetug.power_armor_mod.common.network.packet.ArmorPartPacket;
import com.jetug.power_armor_mod.common.util.constants.Global;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.List;

public class PacketHandler {

	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
									.named(new ResourceLocation(Global.MOD_ID, "main_channel"))
									.clientAcceptedVersions(PROTOCOL_VERSION::equals)
									.serverAcceptedVersions(PROTOCOL_VERSION::equals)
									.networkProtocolVersion(() -> PROTOCOL_VERSION)
									.simpleChannel();
	public static void register() {
		int disc = 0;
		HANDLER.registerMessage(disc++,
				ArmorPartPacket.class,
				ArmorPartPacket::write,
				p -> {
					final ArmorPartPacket msg = new ArmorPartPacket();
					msg.read(p);
					return msg;
				},
				ArmorPartPacket::handle);

		HANDLER.registerMessage(disc++,
				ArmorPartClientPacket.class,
				ArmorPartClientPacket::write,
				p -> {
					final ArmorPartClientPacket msg = new ArmorPartClientPacket();
					msg.read(p);
					return msg;
				},
				ArmorPartClientPacket::handle);

		HANDLER.registerMessage(disc++,
				ArmorClientUpdatePacket.class,
				ArmorClientUpdatePacket::write,
				p -> {
					final ArmorClientUpdatePacket msg = new ArmorClientUpdatePacket();
					msg.read(p);
					return msg;
				},
				ArmorClientUpdatePacket::handle);
	}
	
	/**
	 * Sends a packet to a specific player.<br>
	 * Must be called server side.
	 * */
	public static void sendTo(Object msg, ServerPlayer player) {
		if(!(player instanceof FakePlayer)) {
			HANDLER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}
	}
	
	/**
	 * Sends a packet to the server.<br>
	 * Must be called client side.
	 * */
	public static void sendToServer(Object msg) {
		HANDLER.sendToServer(msg);
	}
	
	/**Server side.*/
	public static void sendToAllPlayers(Object msg, MinecraftServer server) {
		List<ServerPlayer> list = server.getPlayerList().getPlayers();
		for(ServerPlayer e : list) {
			sendTo(msg, e);
		}
	}
}
