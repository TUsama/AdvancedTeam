package com.clefal.teams.network.client.config;

import net.minecraft.network.FriendlyByteBuf;

abstract class S2CTeamConfigBooleanPacket extends S2CTeamConfigChangePacket<Boolean>{


    public S2CTeamConfigBooleanPacket(String name, Boolean config) {
        super(name, config);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeBoolean(config);
    }
}
