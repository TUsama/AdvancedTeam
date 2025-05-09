package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.S2CModPacket;
import com.clefal.teams.utils.ClientHelper;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamInvitedPacket implements S2CModPacket {

    private final String team;

    public S2CTeamInvitedPacket(String team) {
        this.team = team;
    }

    public S2CTeamInvitedPacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void handleClient() {
        ClientHelper.addInviteToast(team);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }

}
