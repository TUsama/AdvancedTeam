package com.clefal.teams.network.server;

import com.clefal.teams.network.ModPacket;
import net.minecraft.server.level.ServerPlayer;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}
