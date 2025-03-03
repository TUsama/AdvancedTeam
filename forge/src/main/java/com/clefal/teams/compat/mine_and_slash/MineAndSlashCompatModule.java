package com.clefal.teams.compat.mine_and_slash;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.modules.compat.ICompatModule;
import com.clefal.teams.modules.internal.HandlerManager;
import com.robertx22.mine_and_slash.mmorpg.SlashRef;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.WithPerms;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;

public class MineAndSlashCompatModule implements ICompatModule {
    public static final MineAndSlashCompatModule INSTANCE = new MineAndSlashCompatModule();
    public boolean isEnabled = false;


    private static MineAndSlashServerConfig serverConfig;

    public static MineAndSlashClientConfig getClientConfig() {
        if (MineAndSlashClientConfig.clientConfig == null) {
            MineAndSlashClientConfig.clientConfig = ConfigApiJava.registerAndLoadConfig(MineAndSlashClientConfig::new, RegisterType.CLIENT);
        }
        return MineAndSlashClientConfig.clientConfig;
    }

    public static MineAndSlashServerConfig getServerConfig() {
        if (MineAndSlashServerConfig.serverConfig == null) {
            MineAndSlashServerConfig.serverConfig = ConfigApiJava.registerAndLoadConfig(MineAndSlashServerConfig::new, RegisterType.BOTH);
        }
        return MineAndSlashServerConfig.serverConfig;
    }


    @Override
    public String getModID() {
        return SlashRef.MODID;
    }

    @Override
    public void whenEnable() {
        AdvancedTeam.LOGGER.info("Detected Mine And Slash, enable MineAndSlashCompatModule!");
        HandlerManager.INSTANCE.registerHandlerPair(MNSPropertyServerHandler.INSTANCE, MNSPropertyClientHandler.INSTANCE);
        if (MineAndSlashCompatModule.getServerConfig().replaceMNSTeam){
            AdvancedTeam.registerAtServer(MineAndSlashPartyCompat.INSTANCE);
        }
        getClientConfig();
        getServerConfig();
        this.isEnabled = true;
    }


    public static class MineAndSlashClientConfig extends Config {

        private static MineAndSlashClientConfig clientConfig;

        public boolean showModProperty = true;


        private MineAndSlashClientConfig() {
            super(AdvancedTeam.id("mine_and_slash_client_config"));
        }
    }

    @WithPerms(opLevel = 2)
    public static class MineAndSlashServerConfig extends Config{

        private static MineAndSlashServerConfig serverConfig;

        private MineAndSlashServerConfig() {
            super(AdvancedTeam.id("mine_and_slash_server_config"));
        }

        @RequiresAction(action = Action.RESTART)
        public boolean replaceMNSTeam = true;
    }
}
