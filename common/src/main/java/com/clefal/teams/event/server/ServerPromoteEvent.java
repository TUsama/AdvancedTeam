package com.clefal.teams.event.server;

import com.clefal.teams.server.ATServerTeam;
import net.minecraft.server.level.ServerPlayer;

public class ServerPromoteEvent extends ServerEvent {
    public final ServerPlayer promoted;
    public final ATServerTeam team;

    public ServerPromoteEvent(ServerPlayer promoted, ATServerTeam team) {
        this.promoted = promoted;
        this.team = team;
    }
}
