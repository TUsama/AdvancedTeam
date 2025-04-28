package com.clefal.teams.network.client.config;

import com.clefal.nirvana_lib.network.S2CModPacket;

abstract class S2CTeamConfigChangePacket<T> implements S2CModPacket {
    String name;
    T config;

    public S2CTeamConfigChangePacket(String name, T config) {
        this.name = name;
        this.config = config;
    }

}
