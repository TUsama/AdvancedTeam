package com.clefal.teams.network.server;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerCreateTeamEvent;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamCreatePacket implements C2SModPacket {

    String team;
    boolean isPublic = false;

    private C2STeamCreatePacket(String team) {
        this.team = team;
    }

    public static C2STeamCreatePacket createNonPublicTeam(String team){
        return new C2STeamCreatePacket(team);
    }

    public static C2STeamCreatePacket createPublicTeam(String team){
        C2STeamCreatePacket c2STeamCreatePacket = new C2STeamCreatePacket(team);
        c2STeamCreatePacket.isPublic = true;
        return c2STeamCreatePacket;
    }


    public C2STeamCreatePacket(FriendlyByteBuf byteBuf) {
        team = byteBuf.readUtf();
        isPublic = byteBuf.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
        to.writeBoolean(isPublic);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        if (isPublic){
            ATServerTeamData.getOrMakeDefault(player.server).createPublicTeam(team, player);
        } else {
            ATServerTeamData.getOrMakeDefault(player.server).createTeam(team, player);
        }

        AdvancedTeam.post(new ServerCreateTeamEvent(team, player));
    }
}
