package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.IHasTeam;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class C2SAcceptApplicationPacket implements C2SModPacket<C2SAcceptApplicationPacket> {
    UUID uuid;

    public C2SAcceptApplicationPacket(UUID uuid) {
        this.uuid = uuid;
    }

    public C2SAcceptApplicationPacket() {
    }


    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUUID(uuid);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        uuid = friendlyByteBuf.readUUID();
    }

    @Override
    public void handleServer(ServerPlayer serverPlayer, C2SAcceptApplicationPacket c2SAcceptApplicationPacket, boolean b) {
        if (((IHasTeam) serverPlayer).hasTeam()){
            ATServerTeam team = ((IHasTeam) serverPlayer).getTeam();
            ServerPlayer player1 = serverPlayer.getServer().getPlayerList().getPlayer(uuid);
            if (player1 != null){
                team.markApplicationAsRemoval(player1.getUUID());
                team.addPlayer(player1);
            } else {
                serverPlayer.sendSystemMessage(Component.translatable("teams.error.no_player"));
            }
        } else {
            serverPlayer.sendSystemMessage(Component.translatable("teams.error.you_are_already_in_a_team"));
        }
    }
}
