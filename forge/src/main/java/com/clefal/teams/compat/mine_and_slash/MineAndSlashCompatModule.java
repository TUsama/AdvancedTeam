package com.clefal.teams.compat.mine_and_slash;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.compat.mine_and_slash.property.MNSStatusEffect;
import com.clefal.teams.event.server.ServerPlayerTickJobEvent;
import com.clefal.teams.modules.compat.ICompatModule;
import com.clefal.teams.modules.internal.HandlerManager;
import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.server.IPropertySender;
import com.clefal.teams.utils.MixinHelper;
import com.robertx22.mine_and_slash.mmorpg.SlashRef;
import com.robertx22.mine_and_slash.uncommon.datasaving.Load;
import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.WithPerms;
import me.fzzyhmstrs.fzzy_config.api.ConfigApiJava;
import me.fzzyhmstrs.fzzy_config.api.RegisterType;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.config.ConfigGroup;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedDouble;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedFloat;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.server.level.ServerPlayer;

public class MineAndSlashCompatModule implements ICompatModule {
    public static final MineAndSlashCompatModule INSTANCE = new MineAndSlashCompatModule();
    public boolean isModuleEnabled = false;

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

            AdvancedTeam.serverBus.<ServerPlayerTickJobEvent>addListener(x -> {
                ServerPlayer player = x.player;
                IPropertySender propertySender = (IPropertySender) player;
                IHasTeam hasTeam = (IHasTeam) player;
                if (hasTeam.hasTeam() && !Load.Unit(player).getStatusEffectsData().exileMap.isEmpty()){
                    propertySender.addUpdate(MNSStatusEffect.KEY);
                }
            });
        }
        getClientConfig();
        getServerConfig();
        isModuleEnabled = true;
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
        public boolean enableTeammateDamageParticle = true;
        public ConfigGroup statusOption = new ConfigGroup("teammates_particle");
        public ValidatedFloat sendParticleWhenWithinRange = new ValidatedFloat(10);
    }
}
