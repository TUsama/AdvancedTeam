package com.clefal.teams.network.client;

import com.clefal.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class S2CTeamInitPacket implements S2CModPacket {

    private final String name;
    private final UUID leader;
    public S2CTeamInitPacket(String name, UUID leader) {
        this.name = name;
        this.leader = leader;
    }

    public S2CTeamInitPacket(FriendlyByteBuf byteBuf) {
        name = byteBuf.readUtf();
        leader = byteBuf.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeUUID(leader);
    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.init(name,leader);
    }
}
