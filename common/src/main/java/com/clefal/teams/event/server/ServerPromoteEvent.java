package com.clefal.teams.event.server;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ServerPromoteEvent extends ServerEvent{
    public final ServerPlayer promoted;

    public ServerPromoteEvent(ServerPlayer promoted) {
        this.promoted = promoted;
    }
}
