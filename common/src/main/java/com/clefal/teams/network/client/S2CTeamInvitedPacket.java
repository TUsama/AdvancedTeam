package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.S2CModPacket;
import com.clefal.teams.client.core.ClientRenderPersistentData;
import com.clefal.teams.server.ATServerTeam;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamInvitedPacket implements S2CModPacket {

    private final String team;

    public S2CTeamInvitedPacket(ATServerTeam team) {
        this.team = team.getName();
    }

    public S2CTeamInvitedPacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void handleClient() {
        ClientRenderPersistentData.getInstance().invitations.add(team);
        Helper.addInviteToast(team);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }

}
