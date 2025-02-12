package com.clefal.teams.network.client.config;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.ClientTeamData;
import com.clefal.teams.network.client.S2CModPacket;

public abstract class S2CTeamConfigChangePacket<T> implements S2CModPacket {
    String name;
    T config;

    public S2CTeamConfigChangePacket(String name, T config) {
        this.name = name;
        this.config = config;
    }

    public static class Public extends S2CTeamConfigBooleanPacket{

        public Public(String name, Boolean config) {
            super(name, config);
        }

        @Override
        public void handleClient() {
            if (config){
                ClientTeamData.INSTANCE.addPublicTeam(name);
            } else {
                ClientTeamData.INSTANCE.removePublicTeam(name);
            }
        }
    }

    public static class EveryoneCanInvite extends S2CTeamConfigBooleanPacket{


        public EveryoneCanInvite(String name, Boolean config) {
            super(name, config);
        }

        @Override
        public void handleClient() {
            ClientTeam.INSTANCE.setCanInvite(config);
        }
    }
}
