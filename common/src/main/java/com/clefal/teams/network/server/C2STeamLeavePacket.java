package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerPlayerLeaveEvent;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.server.IHasTeam;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamLeavePacket implements C2SModPacket<C2STeamLeavePacket> {


    public C2STeamLeavePacket() {}


    @Override
    public void write(FriendlyByteBuf to) {

    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public Class<C2STeamLeavePacket> getSelfClass() {
        return C2STeamLeavePacket.class;
    }


    @Override
    public void handleServer(ServerPlayer serverPlayer, C2STeamLeavePacket c2STeamLeavePacket, boolean b) {
        var team = ((IHasTeam) serverPlayer).getTeam();
        if (ATServerTeamData.getOrMakeDefault(serverPlayer.server).removePlayerFromTeam(serverPlayer)) {
            AdvancedTeam.post(new ServerPlayerLeaveEvent(serverPlayer, team));
        }
    }
}
