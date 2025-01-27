package com.clefal.teams.network.client;

import com.clefal.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;

public class S2CPermissionChangePacket implements S2CModPacket {
    public enum Action{
        PROMOTE,
        DEMOTE
    }
    Action action;
    public S2CPermissionChangePacket(Action action) {
        this.action = action;
    }

    public S2CPermissionChangePacket(FriendlyByteBuf byteBuf) {
        this.action = byteBuf.readEnum(Action.class);
    }

    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.changePermission(this.action == Action.PROMOTE);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeEnum(this.action);
    }
}
