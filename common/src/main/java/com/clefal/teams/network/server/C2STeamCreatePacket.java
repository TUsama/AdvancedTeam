package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerCreateTeamEvent;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class C2STeamCreatePacket implements C2SModPacket<C2STeamCreatePacket> {

    String team;
    boolean isPublic = false;

    private C2STeamCreatePacket(String team) {
        this.team = team;
    }

    public C2STeamCreatePacket() {
    }

    public static C2STeamCreatePacket createNonPublicTeam(String team) {
        return new C2STeamCreatePacket(team);
    }

    public static C2STeamCreatePacket createPublicTeam(String team) {
        C2STeamCreatePacket c2STeamCreatePacket = new C2STeamCreatePacket(team);
        c2STeamCreatePacket.isPublic = true;
        return c2STeamCreatePacket;
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(team);
        to.writeBoolean(isPublic);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        team = friendlyByteBuf.readUtf();
        isPublic = friendlyByteBuf.readBoolean();
    }

    @Override
    public Class<C2STeamCreatePacket> getSelfClass() {
        return C2STeamCreatePacket.class;
    }


    @Override
    public void handleServer(ServerPlayer serverPlayer, C2STeamCreatePacket c2STeamCreatePacket, boolean b) {
        if (isPublic) {
            ATServerTeamData.getOrMakeDefault(serverPlayer.server).createPublicTeam(team, serverPlayer);
        } else {
            ATServerTeamData.getOrMakeDefault(serverPlayer.server).createTeam(team, serverPlayer);
        }

        AdvancedTeam.post(new ServerCreateTeamEvent(team, serverPlayer));
    }
}
