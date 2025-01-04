package com.clefal.teams.network;

import com.clefal.teams.network.server.C2SModPacket;
import com.t2pellet.teams.network.server.*;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.Function;

public class PacketHandlerFabric {

    public static <MSG extends C2SModPacket> ServerPlayNetworking.PlayChannelHandler wrapC2S(Function<FriendlyByteBuf, MSG> decodeFunction) {
        return new ServerHandler<>(decodeFunction);
    }

}
