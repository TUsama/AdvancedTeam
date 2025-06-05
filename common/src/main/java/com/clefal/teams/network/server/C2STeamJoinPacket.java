package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerJoinTeamEvent;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.server.Invitation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class C2STeamJoinPacket implements C2SModPacket<C2STeamJoinPacket> {

    String team;
    public C2STeamJoinPacket(String team) {
        this.team = team;
    }

    public C2STeamJoinPacket() {
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        team = friendlyByteBuf.readUtf();
    }


    @Override
    public void handleServer(ServerPlayer serverPlayer, C2STeamJoinPacket c2STeamJoinPacket, boolean b) {
        Map<String, Invitation> invitations = ((IHasTeam) serverPlayer).getInvitations();
        Invitation invitation = invitations.get(this.team);
        if (invitation != null){
            ATServerTeam team = ATServerTeamData.getOrMakeDefault(serverPlayer.server).getTeam(this.team);
            if (!ATServerTeamData.getOrMakeDefault(serverPlayer.server).addPlayerToTeam(serverPlayer, team)){
                serverPlayer.sendSystemMessage(Component.translatable("teams.error.you_are_already_in_a_team"));
            }
        }
    }
}
