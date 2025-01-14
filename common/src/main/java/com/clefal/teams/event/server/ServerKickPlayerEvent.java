package com.clefal.teams.event.server;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ServerKickPlayerEvent extends ServerEvent{
    public String teamName;
    public UUID toKick;
    public ServerPlayer kicker;

    public ServerKickPlayerEvent(String teamName, UUID toKick, ServerPlayer kicker) {
        this.teamName = teamName;
        this.toKick = toKick;
        this.kicker = kicker;
    }
}
