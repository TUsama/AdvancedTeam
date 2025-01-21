package com.clefal.teams;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.ClientTeamData;

public class AdvancedTeamsHUDClient {

    public static void resetClientTeamStatus() {
        ClientTeam.INSTANCE.reset();
        ClientTeamData.INSTANCE.clear();
    }


}
