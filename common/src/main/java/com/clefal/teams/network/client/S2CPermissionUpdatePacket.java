package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;
import com.clefal.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;

public class S2CPermissionUpdatePacket implements S2CModPacket<S2CPermissionUpdatePacket> {

    UUID leader;
    boolean hasPerm;

    public S2CPermissionUpdatePacket(boolean hasPerm, UUID leader) {
        this.leader = leader;
        this.hasPerm = hasPerm;
    }
    public S2CPermissionUpdatePacket() {

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

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        this.leader = friendlyByteBuf.readUUID();
        this.hasPerm = friendlyByteBuf.readBoolean();
    }

    @Override
    public Class<S2CPermissionUpdatePacket> getSelfClass() {
        return S2CPermissionUpdatePacket.class;
    }
}
