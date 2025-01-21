package com.clefal.teams.network.server;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerKickPlayerEvent;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2STeamKickPacket implements C2SModPacket {

    String name;
    UUID toKick;

    public C2STeamKickPacket(String team, UUID playerToKick) {
        name = team;
        toKick = playerToKick;
    }

    public C2STeamKickPacket(FriendlyByteBuf byteBuf) {
        name = byteBuf.readUtf();
        toKick = byteBuf.readUUID();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name);
        to.writeUUID(toKick);
    }

    @Override
    public void handleServer(ServerPlayer player) {
        ATServerTeam team = ATServerTeamData.getOrMakeDefault(player.server).getTeam(name);
        if (team.playerHasPermissions(player)) {
            ServerPlayer kicked = player.server.getPlayerList().getPlayer(toKick);
            ATServerTeamData.getOrMakeDefault(player.server).removePlayerFromTeam(kicked);
            AdvancedTeam.eventBus.post(new ServerKickPlayerEvent(name, toKick, player));
        } else {
            player.sendSystemMessage(Component.literal("You don't have permission!"));
        }
    }
}
