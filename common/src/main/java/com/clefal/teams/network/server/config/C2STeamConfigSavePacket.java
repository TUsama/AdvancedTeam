package com.clefal.teams.network.server.config;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.IHasTeam;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamConfigSavePacket implements C2SModPacket<C2STeamConfigSavePacket> {
    boolean isPublic;
    boolean everyoneInvite;

    public C2STeamConfigSavePacket(boolean isPublic, boolean everyoneInvite) {
        this.isPublic = isPublic;
        this.everyoneInvite = everyoneInvite;
    }

    public C2STeamConfigSavePacket() {
    }



    @Override
    public void write(FriendlyByteBuf to) {
        to.writeBoolean(isPublic);
        to.writeBoolean(everyoneInvite);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        isPublic = friendlyByteBuf.readBoolean();
        everyoneInvite = friendlyByteBuf.readBoolean();
    }

    @Override
    public Class<C2STeamConfigSavePacket> getSelfClass() {
        return C2STeamConfigSavePacket.class;
    }

    @Override
    public void handleServer(ServerPlayer serverPlayer, C2STeamConfigSavePacket c2STeamConfigSavePacket, boolean b) {
        ATServerTeam team = ((IHasTeam) serverPlayer).getTeam();
        if (team != null){
            team.setPublic(isPublic);
            team.setAllowEveryoneInvite(everyoneInvite);
            team.announceConfigChangeToClients();
        } else {
            AdvancedTeam.LOGGER.warn("no-team player send C2STeamConfigChangePacket! name: {}", serverPlayer.getName().getString());
        }
    }
}
