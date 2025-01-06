package com.clefal.teams.network.server;

import com.clefal.teams.TeamsHUD;
import com.clefal.teams.core.ModTeam;
import com.clefal.teams.core.TeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamLeavePacket implements C2SModPacket {


    public C2STeamLeavePacket() {}

    public C2STeamLeavePacket( FriendlyByteBuf byteBuf) {

    }

    @Override
    public void write(FriendlyByteBuf to) {

    }

    @Override
    public void handleServer(ServerPlayer player) {
        try {
            TeamData.getOrMakeDefault(player.server).removePlayerFromTeam(player);
        } catch (ModTeam.TeamException ex) {
            TeamsHUD.LOGGER.error(ex.getMessage());
        }
    }
}
