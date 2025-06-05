package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.nirvana_lib.relocated.io.vavr.control.Either;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.server.IHasTeam;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.utils.Failure;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.UUID;

public class C2STeamInvitePacket implements C2SModPacket<C2STeamInvitePacket> {


    List<String> to;

    public C2STeamInvitePacket(List<String> to) {
        this.to = to;
    }

    public C2STeamInvitePacket() {
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeCollection(this.to, FriendlyByteBuf::writeUtf);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        to = friendlyByteBuf.readList(FriendlyByteBuf::readUtf);
    }



    @Override
    public void handleServer(ServerPlayer serverPlayer, C2STeamInvitePacket c2STeamInvitePacket, boolean b) {
        for (String s : this.to) {
            UUID to = serverPlayer.server.getProfileCache().get(s).orElseThrow().getId();

            ServerPlayer toPlayer = serverPlayer.server.getPlayerList().getPlayer(to);
            if (toPlayer == null) {
                AdvancedTeam.LOGGER.error("Trying to invite a null player: {}", to);
                return;
            }
            ATServerTeam team = ((IHasTeam) serverPlayer).getTeam();
            if (team == null) {
                serverPlayer.sendSystemMessage(Component.translatable("teams.error.not_in_a_team"));
            } else {
                Either<Failure, Boolean> booleans = ATServerTeamData.getOrMakeDefault(serverPlayer.server).invitePlayerToTeam(toPlayer, team);
                if (!booleans.isRight()) booleans.getLeft().announce(serverPlayer, toPlayer.getName());
            }
        }
    }
}
