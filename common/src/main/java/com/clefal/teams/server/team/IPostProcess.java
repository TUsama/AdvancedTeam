package com.clefal.teams.server.team;

import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.server.level.ServerLevel;

public interface IPostProcess {
    void postProcess(ServerLevel level, ATServerTeamData data, ATServerTeam team);
}
