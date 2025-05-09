package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.C2SModPacket;
import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.network.client.S2CTeamAppliedPacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.Application;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamApplyingPacket implements C2SModPacket {


    String name;
    public C2STeamApplyingPacket(String name) {
        this.name = name;
    }

    public C2STeamApplyingPacket(FriendlyByteBuf byteBuf) {
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
            team.addApplication(new Application(player.getUUID()));
        }
    }
}
