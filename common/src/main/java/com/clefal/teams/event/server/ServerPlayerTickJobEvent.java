package com.clefal.teams.event.server;

import net.minecraft.server.level.ServerPlayer;

public class ServerPlayerTickJobEvent extends ServerEvent{
    public final ServerPlayer player;

    public ServerPlayerTickJobEvent(ServerPlayer player) {
        this.player = player;
    }
}
