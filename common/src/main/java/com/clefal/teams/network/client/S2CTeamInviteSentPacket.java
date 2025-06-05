package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;
import com.clefal.teams.utils.ClientHelper;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamInviteSentPacket implements S2CModPacket<S2CTeamInviteSentPacket> {

    String team;
    String player;

    public S2CTeamInviteSentPacket(String team, String player) {
        this.team = team;
        this.player = player;
    }

    public S2CTeamInviteSentPacket() {

    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
        to.writeUtf(player);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        team = friendlyByteBuf.readUtf();
        player = friendlyByteBuf.readUtf();
    }

    @Override
    public void handleClient() {
        ClientHelper.addInviteSentToast(team, player);
    }
}
