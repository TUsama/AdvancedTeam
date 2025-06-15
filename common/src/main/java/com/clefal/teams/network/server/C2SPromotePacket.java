package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2SPromotePacket implements C2SModPacket<C2SPromotePacket> {
    UUID promoted;

    public C2SPromotePacket(UUID promoted) {
        this.promoted = promoted;
    }

    public C2SPromotePacket() {
    }


    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUUID(promoted);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        promoted = friendlyByteBuf.readUUID();
    }

    @Override
    public Class<C2SPromotePacket> getSelfClass() {
        return C2SPromotePacket.class;
    }

    @Override
    public void handleServer(ServerPlayer serverPlayer, C2SPromotePacket c2SPromotePacket, boolean b) {
        ATServerTeam team = ATServerTeamData.getOrMakeDefault(serverPlayer.server).getTeam(serverPlayer);
        if (team.hasPlayer(this.promoted)){
            team.getOnlinePlayers().find(x -> x.getUUID().equals(this.promoted)).forEach(team::promote);
        }
    }
}
