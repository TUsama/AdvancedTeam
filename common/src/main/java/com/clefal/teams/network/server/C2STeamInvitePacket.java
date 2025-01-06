package com.clefal.teams.network.server;

import com.clefal.teams.TeamsHUD;
import com.clefal.teams.core.IHasTeam;
import com.clefal.teams.core.ModTeam;
import com.clefal.teams.core.TeamData;
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

        ModTeam team = ((IHasTeam) player).getTeam();
        if (team == null) {
            TeamsHUD.LOGGER.error("{} tried inviting {} but they are not in a team..", player.getName().getString(), toPlayer.getName().getString());
        } else {
            TeamData.getOrMakeDefault(player.server).invitePlayerToTeam(toPlayer, team);
        }
    }
}
