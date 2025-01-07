package com.clefal.teams.network.server;

import com.clefal.teams.TeamsHUD;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import net.minecraft.network.FriendlyByteBuf;
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
        if (player != null && team.playerHasPermissions(player)) {
            ServerPlayer kicked = player.server.getPlayerList().getPlayer(toKick);
            try {
                ATServerTeamData.getOrMakeDefault(player.server).removePlayerFromTeam(kicked);
            } catch (ATServerTeam.TeamException ex) {
                TeamsHUD.LOGGER.error(ex.getMessage());
            }
        } else {
            TeamsHUD.LOGGER.error("Received packet to kick player, but the sender did not have permissions");
        }
    }
}
