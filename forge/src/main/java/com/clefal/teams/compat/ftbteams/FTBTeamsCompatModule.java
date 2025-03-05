package com.clefal.teams.compat.ftbteams;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.modules.compat.ICompatModule;
import me.fzzyhmstrs.fzzy_config.annotations.WithPerms;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;

public class FTBTeamsCompatModule implements ICompatModule {
    public static final FTBTeamsCompatModule INSTANCE = new FTBTeamsCompatModule();
    public boolean isModuleEnabled = false;

    public static FTBTServerConfig getServerConfig() {
        if (FTBTServerConfig.serverConfig == null) {
            FTBTServerConfig.serverConfig = ConfigApiJava.registerAndLoadConfig(FTBTServerConfig::new, RegisterType.BOTH);
        }
        return FTBTServerConfig.serverConfig;
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

    }

    @WithPerms(opLevel = 2)
    public static class FTBTServerConfig extends Config {

        private static FTBTServerConfig serverConfig;
        public boolean enableOfflineSupport = true;

        private FTBTServerConfig() {
            super(AdvancedTeam.id("ftb_teams_server_config"));
        }
    }
}
