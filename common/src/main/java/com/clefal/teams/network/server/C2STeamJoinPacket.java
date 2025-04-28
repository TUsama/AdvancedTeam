package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.C2SModPacket;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerJoinTeamEvent;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamJoinPacket implements C2SModPacket {

    String team;
    public C2STeamJoinPacket(String team) {
        this.team = team;
    }

    public C2STeamJoinPacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }
    @Override
    public void handleServer(ServerPlayer player) {
        ATServerTeam team = ATServerTeamData.getOrMakeDefault(player.server).getTeam(this.team);
        if (!ATServerTeamData.getOrMakeDefault(player.server).addPlayerToTeam(player, team)){
            player.sendSystemMessage(Component.translatable("teams.error.you_are_already_in_a_team"));
        }
    }
}
