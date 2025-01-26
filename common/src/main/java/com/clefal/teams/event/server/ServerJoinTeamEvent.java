package com.clefal.teams.event.server;

import com.clefal.teams.server.ATServerTeam;
import net.minecraft.server.level.ServerPlayer;

public class ServerJoinTeamEvent extends ServerEvent{
    public ATServerTeam team;
    public ServerPlayer joiner;

    public ServerJoinTeamEvent(ATServerTeam teamReadableName, ServerPlayer joiner) {
        this.team = teamReadableName;
        this.joiner = joiner;
    }
}
