package com.clefal.teams.event.server;

import com.clefal.teams.server.ATServerTeam;
import net.minecraft.server.level.ServerPlayer;

public class ServerPlayerLeaveEvent extends ServerEvent{
    public ServerPlayer leaver;
    public ATServerTeam leavedTeam;

    public ServerPlayerLeaveEvent(ServerPlayer leaver, ATServerTeam leavedTeam) {
        this.leaver = leaver;
        this.leavedTeam = leavedTeam;
    }

}
