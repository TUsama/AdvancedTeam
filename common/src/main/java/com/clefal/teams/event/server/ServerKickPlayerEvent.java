package com.clefal.teams.event.server;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ServerKickPlayerEvent extends ServerEvent{
    public String teamName;
    public ServerPlayer toKick;

    public ServerKickPlayerEvent(String teamName, ServerPlayer toKick) {
        this.teamName = teamName;
        this.toKick = toKick;
    }
}
