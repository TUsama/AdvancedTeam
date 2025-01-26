package com.clefal.teams.network.server;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerPlayerLeaveEvent;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamLeavePacket implements C2SModPacket {


    public C2STeamLeavePacket() {}

    public C2STeamLeavePacket( FriendlyByteBuf byteBuf) {

    }

    @Override
    public void write(FriendlyByteBuf to) {

    }

    @Override
    public void handleServer(ServerPlayer player) {
        if (ATServerTeamData.getOrMakeDefault(player.server).removePlayerFromTeam(player)) {
            AdvancedTeam.post(new ServerPlayerLeaveEvent(player));
        }
    }
}
