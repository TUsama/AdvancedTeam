package com.clefal.teams.network.server;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2STeamInvitePacket implements C2SModPacket {


    String to;

    public C2STeamInvitePacket(String to) {
        this.to = to;
    }

    public C2STeamInvitePacket(FriendlyByteBuf byteBuf) {
        to = byteBuf.readUtf();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(this.to);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        UUID to = player.server.getProfileCache().get(this.to).orElseThrow().getId();

        ServerPlayer toPlayer = player.server.getPlayerList().getPlayer(to);

        ATServerTeam team = ((IHasTeam) player).getTeam();
        if (team == null) {
            AdvancedTeam.LOGGER.error("{} tried inviting {} but they are not in a team..", player.getName().getString(), toPlayer.getName().getString());
        } else {
            ATServerTeamData.getOrMakeDefault(player.server).invitePlayerToTeam(toPlayer, team);
        }
    }
}
