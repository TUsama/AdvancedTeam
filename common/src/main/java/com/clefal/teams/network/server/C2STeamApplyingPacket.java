package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.network.client.S2CTeamAppliedPacket;

import com.clefal.teams.server.Application;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamApplyingPacket implements C2SModPacket<C2STeamApplyingPacket> {


    String name;
    public C2STeamApplyingPacket(String name) {
        this.name = name;
    }

    public C2STeamApplyingPacket() {
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        this.name = friendlyByteBuf.readUtf();
    }


    @Override
    public void handleServer(ServerPlayer serverPlayer, C2STeamApplyingPacket c2SModPacket, boolean b) {
        ATServerTeam team = ATServerTeamData.getOrMakeDefault(serverPlayer.server).getTeam(name);
        if (team == null) {
            serverPlayer.sendSystemMessage(Component.literal("Team doesn't exist"));
        } else {
            team.addApplication(new Application(serverPlayer.getUUID()));
        }
    }
}
