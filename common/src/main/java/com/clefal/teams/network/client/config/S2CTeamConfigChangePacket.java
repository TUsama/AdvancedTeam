package com.clefal.teams.network.client.config;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;

abstract class S2CTeamConfigChangePacket<T, MSG extends S2CModPacket<MSG>> implements S2CModPacket<MSG> {
    String name;
    T config;

    public S2CTeamConfigChangePacket(String name, T config) {
        this.name = name;
        this.config = config;
    }

    public S2CTeamConfigChangePacket() {
    }
}
