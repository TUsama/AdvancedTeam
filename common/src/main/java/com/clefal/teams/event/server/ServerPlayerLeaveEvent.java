package com.clefal.teams.event.server;

import net.minecraft.server.level.ServerPlayer;

public class ServerPlayerLeaveEvent extends ServerEvent{
    public ServerPlayer leaver;

    public ServerPlayerLeaveEvent(ServerPlayer leaver) {
        this.leaver = leaver;
    }
}
