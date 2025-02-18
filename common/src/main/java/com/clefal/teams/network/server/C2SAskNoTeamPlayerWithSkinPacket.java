package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;
import com.clefal.teams.client.gui.util.PlayerWithSkin;
import com.clefal.teams.network.client.S2CReturnPlayerWithSkinPacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.IHasTeam;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;


public class C2SAskNoTeamPlayerWithSkinPacket implements C2SModPacket {

    public C2SAskNoTeamPlayerWithSkinPacket() {
    }

    public C2SAskNoTeamPlayerWithSkinPacket(FriendlyByteBuf buf) {
    }

    @Override
    public void handleServer(ServerPlayer player) {
        Services.PLATFORM.sendToClient(new S2CReturnPlayerWithSkinPacket(List.ofAll(player.getServer().getPlayerList().getPlayers())
                .filter(x -> !((IHasTeam) x).hasTeam())
                .map(x -> {
                    var properties = player.getGameProfile().getProperties();
                    Property skin = null;
                    if (properties.containsKey("textures")) {
                        skin = properties.get("textures").iterator().next();
                    }
                    return new PlayerWithSkin(x.getName().getString(), x.getUUID(), skin != null ? skin.getValue() : "", skin != null ?
                            skin.getSignature() != null ? skin.getSignature() : "" : "");
                }), S2CReturnPlayerWithSkinPacket.Usage.INVITATION), player);
    }

    @Override
    public void write(FriendlyByteBuf to) {

    }
}
