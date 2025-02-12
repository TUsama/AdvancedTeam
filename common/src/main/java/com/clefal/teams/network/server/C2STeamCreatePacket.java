package com.clefal.teams.network.server;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerCreateTeamEvent;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamCreatePacket implements C2SModPacket {

    String team;

    public C2STeamCreatePacket(String team) {
        this.team = team;
    }

    public C2STeamCreatePacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        ATServerTeamData.getOrMakeDefault(player.server).createTeam(team, player);
        AdvancedTeam.post(new ServerCreateTeamEvent(team, player));
    }
}
