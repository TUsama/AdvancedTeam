package com.clefal.teams.network;

import com.clefal.teams.network.client.S2CModPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class ClientPacketHandlerFabric {

    public static <MSG extends S2CModPacket> ClientPlayNetworking.PlayChannelHandler wrapS2C(Function<FriendlyByteBuf, MSG> decodeFunction) {
        return new ClientHandler<>(decodeFunction);
    }

}
