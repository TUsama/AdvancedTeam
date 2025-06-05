package com.clefal.teams.network.client.config;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.ClientTeamData;
import net.minecraft.network.FriendlyByteBuf;

public abstract class S2CTeamConfigBooleanPacket<MSG extends S2CModPacket<MSG>> extends S2CTeamConfigChangePacket<Boolean, MSG>{


    public S2CTeamConfigBooleanPacket(String name, Boolean config) {
        super(name, config);
    }

    public S2CTeamConfigBooleanPacket() {
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeBoolean(config);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        name = friendlyByteBuf.readUtf();
        config = friendlyByteBuf.readBoolean();
    }

    public static class Public extends S2CTeamConfigBooleanPacket<Public>{

        public Public(String name, Boolean config) {
            super(name, config);
        }

        public Public() {
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

    public static class EveryoneCanInvite extends S2CTeamConfigBooleanPacket<EveryoneCanInvite>{


        public EveryoneCanInvite(String name, Boolean config) {
            super(name, config);
        }

        public EveryoneCanInvite() {
        }
        @Override
        public void handleClient() {
            ClientTeam.INSTANCE.setCanInvite(config);
        }
    }
}
