package com.clefal.teams.network.server;

import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.IHasTeam;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2SAcceptApplicationPacket implements C2SModPacket{
    UUID uuid;

    public C2SAcceptApplicationPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public C2SAcceptApplicationPacket(FriendlyByteBuf byteBuf) {
        uuid = byteBuf.readUUID();
    }

    @Override
    public void handleServer(ServerPlayer player) {
        if (((IHasTeam) player).hasTeam()){
            ATServerTeam team = ((IHasTeam) player).getTeam();
            ServerPlayer player1 = player.getServer().getPlayerList().getPlayer(uuid);
            if (player1 != null){
                team.addPlayer(player1);
            } else {
                player.sendSystemMessage(Component.translatable("teams.error.no_player"));
            }
        } else {
            player.sendSystemMessage(Component.translatable("teams.error.you_are_already_in_a_team"));
        }


    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUUID(uuid);
    }
}
