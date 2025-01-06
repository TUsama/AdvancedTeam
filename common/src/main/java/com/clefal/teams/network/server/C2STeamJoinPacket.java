package com.clefal.teams.network.server;

import com.clefal.teams.TeamsHUD;
import com.clefal.teams.core.ModTeam;
import com.clefal.teams.core.TeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamJoinPacket implements C2SModPacket {

    String team;
    public C2STeamJoinPacket(String team) {
        this.team = team;
    }

    public C2STeamJoinPacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }
    @Override
    public void handleServer(ServerPlayer player) {
        ModTeam team = TeamData.getOrMakeDefault(player.server).getTeam(this.team);
        try {
            TeamData.getOrMakeDefault(player.server).addPlayerToTeam(player, team);
        } catch (ModTeam.TeamException ex) {
            TeamsHUD.LOGGER.error("Failed to join team: {}", team);
            TeamsHUD.LOGGER.error(ex.getMessage());
        }
    }
}
