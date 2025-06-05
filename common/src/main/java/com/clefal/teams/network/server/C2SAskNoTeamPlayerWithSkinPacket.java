package com.clefal.teams.network.server;

import com.clefal.nirvana_lib.network.newtoolchain.C2SModPacket;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;
import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.client.gui.util.PlayerWithSkin;
import com.clefal.teams.network.client.S2CReturnPlayerWithSkinPacket;
import com.clefal.teams.server.IHasTeam;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;


public class C2SAskNoTeamPlayerWithSkinPacket implements C2SModPacket<C2SAskNoTeamPlayerWithSkinPacket> {

    public C2SAskNoTeamPlayerWithSkinPacket() {
    }



    @Override
    public void write(FriendlyByteBuf to) {

    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {

    }

    @Override
    public void handleServer(ServerPlayer serverPlayer, C2SAskNoTeamPlayerWithSkinPacket c2SAskNoTeamPlayerWithSkinPacket, boolean b) {
        NetworkUtils.sendToClient(new S2CReturnPlayerWithSkinPacket(List.ofAll(serverPlayer.getServer().getPlayerList().getPlayers())
                .filter(x -> !((IHasTeam) x).hasTeam())
                .map(x -> {
                    var properties = serverPlayer.getGameProfile().getProperties();
                    Property skin = null;
                    if (properties.containsKey("textures")) {
                        skin = properties.get("textures").iterator().next();
                    }
                    return new PlayerWithSkin(x.getName().getString(), x.getUUID(), skin != null ? skin.getValue() : "", skin != null ?
                            skin.getSignature() != null ? skin.getSignature() : "" : "");
                }), S2CReturnPlayerWithSkinPacket.Usage.INVITATION), serverPlayer);
    }
}
