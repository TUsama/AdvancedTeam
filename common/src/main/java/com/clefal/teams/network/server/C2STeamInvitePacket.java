package com.clefal.teams.network.server;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.UUID;

public class C2STeamInvitePacket implements C2SModPacket {


    List<String> to;

    public C2STeamInvitePacket(List<String> to) {
        this.to = to;
    }

    public C2STeamInvitePacket(FriendlyByteBuf byteBuf) {
        to = byteBuf.readList(FriendlyByteBuf::readUtf);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeCollection(this.to, FriendlyByteBuf::writeUtf);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        for (String s : this.to) {
            UUID to = player.server.getProfileCache().get(s).orElseThrow().getId();

            ServerPlayer toPlayer = player.server.getPlayerList().getPlayer(to);
            if (toPlayer == null) {
                AdvancedTeam.LOGGER.error("Trying to invite a null player: {}", to);
                return;
            }
            ATServerTeam team = ((IHasTeam) player).getTeam();
            if (team == null) {
                player.sendSystemMessage(Component.translatable("teams.error.not_in_a_team"));
            } else {
                if (!ATServerTeamData.getOrMakeDefault(player.server).invitePlayerToTeam(toPlayer, team)) player.sendSystemMessage(Component.translatable("teams.error.alreadyinteam", to));
            }
        }

    }
}
