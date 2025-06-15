package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerKickPlayerEvent;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2STeamKickPacket implements C2SModPacket<C2STeamKickPacket> {

    String name;
    UUID toKick;

    public C2STeamKickPacket(String team, UUID playerToKick) {
        name = team;
        toKick = playerToKick;
    }

    public C2STeamKickPacket() {
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeUUID(toKick);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        name = friendlyByteBuf.readUtf();
        toKick = friendlyByteBuf.readUUID();
    }

    @Override
    public Class<C2STeamKickPacket> getSelfClass() {
        return C2STeamKickPacket.class;
    }

    @Override
    public void handleServer(ServerPlayer serverPlayer, C2STeamKickPacket c2STeamKickPacket, boolean b) {
        ATServerTeam team = ATServerTeamData.getOrMakeDefault(serverPlayer.server).getTeam(name);
        if (team.playerHasPermissions(serverPlayer)) {
            ServerPlayer kicked = serverPlayer.server.getPlayerList().getPlayer(toKick);
            ATServerTeamData.getOrMakeDefault(serverPlayer.server).removePlayerFromTeam(kicked);
            AdvancedTeam.post(new ServerKickPlayerEvent(serverPlayer, kicked, name));
        } else {
            serverPlayer.sendSystemMessage(Component.literal("You don't have permission!"));
        }
    }
}
