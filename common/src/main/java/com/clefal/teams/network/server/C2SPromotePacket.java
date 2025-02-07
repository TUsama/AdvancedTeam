package com.clefal.teams.network.server;

import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2SPromotePacket implements C2SModPacket{
    UUID promoted;

    public C2SPromotePacket(UUID promoted) {
        this.promoted = promoted;
    }

    public C2SPromotePacket(FriendlyByteBuf byteBuf) {
        promoted = byteBuf.readUUID();
    }

    @Override
    public void handleServer(ServerPlayer player) {
        ATServerTeam team = ATServerTeamData.getOrMakeDefault(player.server).getTeam(player);
        if (team.hasPlayer(this.promoted)){
            team.getOnlinePlayers().find(x -> x.getUUID().equals(this.promoted)).forEach(team::promote);
        }

    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUUID(promoted);
    }
}
