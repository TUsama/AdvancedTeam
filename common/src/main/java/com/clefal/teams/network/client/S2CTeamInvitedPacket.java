package com.clefal.teams.network.client;

import com.clefal.teams.client.gui.toast.ToastInvited;
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
        Helper.addInviteToast(team);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }

}
