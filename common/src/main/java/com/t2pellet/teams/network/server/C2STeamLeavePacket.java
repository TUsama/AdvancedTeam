package com.t2pellet.teams.network.server;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.ModTeam;
import com.t2pellet.teams.core.TeamDB;
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
            TeamDB.getOrMakeDefault(player.server).removePlayerFromTeam(player);
        } catch (ModTeam.TeamException ex) {
            TeamsHUD.LOGGER.error(ex.getMessage());
        }
    }
}
