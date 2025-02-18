package com.clefal.teams.network.client.config;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.ClientTeamData;
import net.minecraft.network.FriendlyByteBuf;

public abstract class S2CTeamConfigBooleanPacket extends S2CTeamConfigChangePacket<Boolean>{


    public S2CTeamConfigBooleanPacket(String name, Boolean config) {
        super(name, config);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeBoolean(config);
    }

    public static class Public extends S2CTeamConfigBooleanPacket{

        public Public(String name, Boolean config) {
            super(name, config);
        }

        public Public(FriendlyByteBuf byteBuf) {
            this(byteBuf.readUtf(), byteBuf.readBoolean());
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

        public EveryoneCanInvite(FriendlyByteBuf byteBuf) {
            this(byteBuf.readUtf(), byteBuf.readBoolean());
        }
        @Override
        public void handleClient() {
            ClientTeam.INSTANCE.setCanInvite(config);
        }
    }
}
