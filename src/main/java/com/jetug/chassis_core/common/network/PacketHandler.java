package com.jetug.chassis_core.common.network;

import com.jetug.chassis_core.common.network.packet.*;
import com.jetug.chassis_core.common.data.constants.Global;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.List;

public class PacketHandler {
	private static final String PROTOCOL_VERSION = Integer.toString(1);
	private static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
									.named(new ResourceLocation(Global.MOD_ID, "network"))
									.clientAcceptedVersions(PROTOCOL_VERSION::equals)
									.serverAcceptedVersions(PROTOCOL_VERSION::equals)
									.networkProtocolVersion(() -> PROTOCOL_VERSION)
									.simpleChannel();
	private static int disc = 0;

	public static void register() {
		HANDLER.registerMessage(disc++, ActionPacket.class	   , ActionPacket::write	 , ActionPacket::read	 , ActionPacket::handle		);
		HANDLER.registerMessage(disc++, PowerArmorPacket.class , PowerArmorPacket::write , PowerArmorPacket::read, PowerArmorPacket::handle );
		HANDLER.registerMessage(disc++, InteractPacket.class   , InteractPacket::write	 , InteractPacket::read	 , InteractPacket::handle	);
		HANDLER.registerMessage(disc++, HurtPacket.class	   , HurtPacket::write		 , HurtPacket::read	 	 , HurtPacket::handle		);
		HANDLER.registerMessage(disc++, GenericPacket.class	   , GenericPacket::write	 , GenericPacket::read	 , GenericPacket::handle	);
	}

	public static void sendToServer(Object msg) {
		HANDLER.sendToServer(msg);
	}

	public static void sendTo(Object msg, ServerPlayer player) {
		if(!(player instanceof FakePlayer)) {
			HANDLER.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
		}
	}

	public static void sendToAllPlayers(Object msg) {
		var server = ServerLifecycleHooks.getCurrentServer();
		List<ServerPlayer> list = server.getPlayerList().getPlayers();
		for(ServerPlayer e : list) {
			sendTo(msg, e);
		}
	}
}
