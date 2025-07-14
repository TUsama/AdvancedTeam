package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;
import com.clefal.teams.client.core.ClientRenderPersistentData;
import com.clefal.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamClearPacket implements S2CModPacket<S2CTeamClearPacket> {

    public S2CTeamClearPacket() {
    }


    @Override
    public void write(FriendlyByteBuf to) {

    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public Class<S2CTeamClearPacket> getSelfClass() {
        return S2CTeamClearPacket.class;
    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.reset();
        //don't think we need to reset invitations.
        ClientRenderPersistentData.getInstance().applications.clear();
    }
}
