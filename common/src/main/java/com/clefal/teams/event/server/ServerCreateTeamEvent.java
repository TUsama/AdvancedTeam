package com.clefal.teams.event.server;

import net.minecraft.server.level.ServerPlayer;

public class ServerCreateTeamEvent extends ServerEvent{
    public String teamReadableName;
    public ServerPlayer player;

    public ServerCreateTeamEvent(String teamReadableName, ServerPlayer player) {
        this.teamReadableName = teamReadableName;
        this.player = player;
    }
}
