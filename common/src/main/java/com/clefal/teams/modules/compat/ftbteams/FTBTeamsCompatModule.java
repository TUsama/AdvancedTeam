package com.clefal.teams.modules.compat.ftbteams;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.modules.compat.ICompatModule;

public class FTBTeamsCompatModule implements ICompatModule {
    public static FTBTeamsCompatModule INSTANCE;

    public static FTBTeamsCompatModule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FTBTeamsCompatModule();
        }
        return INSTANCE;
    }
    @Override
    public String getModID() {
        return "ftbteams";
    }

    @Override
    public void whenEnable() {
        AdvancedTeam.registerAtServer(new FTBTeamsPartyCompat());
    }
}
