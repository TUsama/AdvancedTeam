package com.clefal.teams.event.server;

import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class ServerKickPlayerEvent extends ServerEvent{
    public String teamName;
    public ServerPlayer toKick;
    public ServerPlayer kicker;

    public ServerKickPlayerEvent(ServerPlayer kicker, ServerPlayer toKick, String teamName) {
        this.kicker = kicker;
        this.toKick = toKick;
        this.teamName = teamName;
    }

}
