package com.clefal.teams.network.client;

import com.clefal.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class S2CPermissionUpdatePacket implements S2CModPacket {

    UUID leader;
    boolean hasPerm;

    public S2CPermissionUpdatePacket(boolean hasPerm, UUID leader) {
        this.leader = leader;
        this.hasPerm = hasPerm;
    }

    public S2CPermissionUpdatePacket(FriendlyByteBuf byteBuf) {
        this.leader = byteBuf.readUUID();
        this.hasPerm = byteBuf.readBoolean();
    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.changeLeader(this.leader);
        ClientTeam.INSTANCE.updatePermission(this.hasPerm);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUUID(this.leader);
        to.writeBoolean(hasPerm);
    }
}
