package com.clefal.teams.event.server;

import net.minecraft.server.level.ServerPlayer;

public class ServerOnPlayerOnlineEvent extends ServerEvent{
    public final ServerPlayer player;

    public ServerOnPlayerOnlineEvent(ServerPlayer player) {
        this.player = player;
    }
}
