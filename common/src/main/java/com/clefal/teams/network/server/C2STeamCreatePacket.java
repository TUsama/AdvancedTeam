package com.clefal.teams.network.server;

import com.clefal.teams.TeamsHUD;
import com.clefal.teams.core.TeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamCreatePacket implements C2SModPacket {

    String team;

    public C2STeamCreatePacket(String team) {
        this.team =  team;
    }

    public C2STeamCreatePacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        try {
            TeamData.getOrMakeDefault(player.server).addTeam(team, player);
        } catch (Exception e) {
            TeamsHUD.LOGGER.error(e.getMessage());
        }
    }
}
