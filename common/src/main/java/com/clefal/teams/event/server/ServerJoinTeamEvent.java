package com.clefal.teams.event.server;

import com.clefal.teams.server.ATServerTeam;
import net.minecraft.server.level.ServerPlayer;

public class ServerJoinTeamEvent extends ServerEvent{
    public ATServerTeam teamReadableName;
    public ServerPlayer player;

    public ServerJoinTeamEvent(ATServerTeam teamReadableName, ServerPlayer player) {
        this.teamReadableName = teamReadableName;
        this.player = player;
    }
}
