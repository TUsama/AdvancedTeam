package com.clefal.teams.server;

import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public interface IHasTeam {

    // Returns whether target is in team
    boolean hasTeam();

    // Returns target's team, or null if not in a team
    ATServerTeam getTeam();

    void setTeam(ATServerTeam team);

    boolean isTeammate(ServerPlayer other);

    List<Invitation> getInvitations();
}
