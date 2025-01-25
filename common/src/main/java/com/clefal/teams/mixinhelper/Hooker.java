package com.clefal.teams.mixinhelper;

import com.clefal.teams.network.client.S2CTeamPlayerDataPacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class Hooker {

    public static void UpdatePropertyInfoForEveryone(ServerPlayer player){
        ATServerTeam team = ATServerTeamData.getOrMakeDefault(player.server).getTeam(player);
        if (team != null) {
            List<ServerPlayer> players = new ArrayList<>(team.getOnlinePlayers());
            Services.PLATFORM.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.UPDATE), players);
        }
    }
}
