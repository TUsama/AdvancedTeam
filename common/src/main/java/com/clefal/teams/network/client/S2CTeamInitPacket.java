package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;
import com.clefal.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class S2CTeamInitPacket implements S2CModPacket<S2CTeamInitPacket> {

    private String name;
    private UUID leader;
    public S2CTeamInitPacket(String name, UUID leader) {
        this.name = name;
        this.leader = leader;
    }

    public S2CTeamInitPacket() {

    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeUUID(leader);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        name = friendlyByteBuf.readUtf();
        leader = friendlyByteBuf.readUUID();
    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.init(name,leader);
    }
}
