package com.clefal.teams.network.client;

import com.clefal.teams.client.core.ClientTeam;
import net.minecraft.network.FriendlyByteBuf;

public class S2CPromotePacket implements S2CModPacket {
    @Override
    public void handleClient() {
        ClientTeam.INSTANCE.promote();
    }

    @Override
    public void write(FriendlyByteBuf to) {
    }
}
