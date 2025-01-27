package com.clefal.teams.compat.mine_and_slash;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.compat.ICompatModule;
import com.clefal.teams.server.propertyhandler.HandlerManager;
import com.robertx22.mine_and_slash.mmorpg.SlashRef;

public class MineAndSlashCompatModule implements ICompatModule {
    public static final MineAndSlashCompatModule INSTANCE = new MineAndSlashCompatModule();

    @Override
    public String getModID() {
        return SlashRef.MODID;
    }

    @Override
    public void whenEnable() {
        AdvancedTeam.LOGGER.info("Detected Mine And Slash, enable MineAndSlashCompatModule!");
        HandlerManager.INSTANCE.registerHandlerPair(MNSPropertyServerHandler.INSTANCE, MNSPropertyClientHandler.INSTANCE);
        MNSPropertyClientHandler.getConfig();
    }


}
