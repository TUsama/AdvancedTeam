package com.clefal.teams.modules.compat.ftbteams;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.modules.compat.ICompatModule;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.WithPerms;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;

public class FTBTeamsCompatModule implements ICompatModule {
    public static FTBTeamsCompatModule INSTANCE;
    public static boolean isModuleEnabled = false;

    public static FTBTeamsServerConfig getServerConfig() {
        if (FTBTeamsServerConfig.serverConfig == null) {
            FTBTeamsServerConfig.serverConfig = ConfigApiJava.registerAndLoadConfig(FTBTeamsServerConfig::new, RegisterType.BOTH);
        }
        return FTBTeamsServerConfig.serverConfig;
    }

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
        AdvancedTeam.LOGGER.info("Detected FTB Teams, enable FTBTeamsCompatModule!");
        isModuleEnabled = true;
        getServerConfig();
        if (getServerConfig().enableFTBPartyCompat) AdvancedTeam.registerAtServer(new FTBTeamsPartyCompat());

    }

    @WithPerms(opLevel = 2)
    public static class FTBTeamsServerConfig extends Config {

        private static FTBTeamsServerConfig serverConfig;

        @RequiresAction(action = Action.RESTART)
        public boolean enableFTBPartyCompat = true;
        public boolean enableOfflineSupport = true;


        private FTBTeamsServerConfig() {
            super(AdvancedTeam.id("ftb_teams_server_config"));
        }
    }
}
