package com.clefal.teams.network.server.config;

import com.clefal.nirvana_lib.network.C2SModPacket;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.IHasTeam;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamConfigSavePacket implements C2SModPacket {
    boolean isPublic;
    boolean everyoneInvite;

    public C2STeamConfigSavePacket(boolean isPublic, boolean everyoneInvite) {
        this.isPublic = isPublic;
        this.everyoneInvite = everyoneInvite;
    }

    public C2STeamConfigSavePacket(FriendlyByteBuf buf) {
        isPublic = buf.readBoolean();
        everyoneInvite = buf.readBoolean();
    }

    @Override
    public void handleServer(ServerPlayer player) {
        ATServerTeam team = ((IHasTeam) player).getTeam();
        if (team != null){
            team.setPublic(isPublic);
            team.setAllowEveryoneInvite(everyoneInvite);
            team.announceConfigChangeToClient();
        } else {
            AdvancedTeam.LOGGER.warn("no-team player send C2STeamConfigChangePacket! name: {}", player.getName().getString());
        }

    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeBoolean(isPublic);
        to.writeBoolean(everyoneInvite);
    }
}
