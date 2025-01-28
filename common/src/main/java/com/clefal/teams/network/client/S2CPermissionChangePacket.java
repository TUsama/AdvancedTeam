package com.clefal.teams.network.client;

import com.clefal.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class S2CPermissionChangePacket implements S2CModPacket {

    UUID leader;

    public S2CPermissionChangePacket(UUID leader) {
        this.leader = leader;
    }

    public S2CPermissionChangePacket(FriendlyByteBuf byteBuf) {
        this.leader = byteBuf.readUUID();
    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.changeLeader(this.leader);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUUID(this.leader);
    }
}
