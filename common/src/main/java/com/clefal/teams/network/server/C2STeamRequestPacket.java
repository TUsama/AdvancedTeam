package com.clefal.teams.network.server;

import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.network.client.S2CTeamRequestedPacket;
import com.clefal.teams.platform.Services;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamRequestPacket implements C2SModPacket {


    String name;
    public C2STeamRequestPacket(String name) {
        this.name = name;
    }

    public C2STeamRequestPacket(FriendlyByteBuf byteBuf) {
        name = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        ATServerTeam team = ATServerTeamData.getOrMakeDefault(player.server).getTeam(name);
        if (team == null) {
            player.sendSystemMessage(Component.literal("Team doesn't exist"));
        } else {
            // Get first online player in list of seniority
            var list = player.server.getPlayerList();
            ServerPlayer seniorPlayer = list.getPlayer(team.getLeader());
            Services.PLATFORM.sendToClient(new S2CTeamRequestedPacket(name, player.getUUID()), seniorPlayer);
        }
    }
}
