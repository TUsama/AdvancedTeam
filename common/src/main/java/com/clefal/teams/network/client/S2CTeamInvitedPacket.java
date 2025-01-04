package com.clefal.teams.network.client;

import com.clefal.teams.client.TeamsHUDClient;
import com.clefal.teams.core.ModTeam;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamInvitedPacket implements S2CModPacket {

    private final String team;

    public S2CTeamInvitedPacket(ModTeam team) {
        this.team = team.getName();
    }

    public S2CTeamInvitedPacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void handleClient() {
        TeamsHUDClient.handleTeamInvitedPacket(team);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }

}
