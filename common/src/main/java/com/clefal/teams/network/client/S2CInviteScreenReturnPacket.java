package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;
import com.clefal.teams.client.gui.util.PlayerWithSkin;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;

public class S2CInviteScreenReturnPacket implements S2CModPacket {

    List<PlayerWithSkin> playersName;

    public S2CInviteScreenReturnPacket(List<PlayerWithSkin> playersName) {
        this.playersName = playersName;
    }

    public S2CInviteScreenReturnPacket(FriendlyByteBuf byteBuf) {
        java.util.List<PlayerWithSkin> objects = byteBuf.readCollection(x -> new ArrayList<>(), buf -> new PlayerWithSkin(
                buf.readUtf(), buf.readUUID(), buf.readUtf(), buf.readUtf()
        ));
        playersName = List.ofAll(objects);
    }


    @Override
    public void handleClient() {
        Helper.tryInitScreenEntryList(playersName);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeCollection(playersName.toJavaList(), (buf, playerWithSkin) -> {
            buf.writeUtf(playerWithSkin.name());
            buf.writeUUID(playerWithSkin.uuid());
            buf.writeUtf(playerWithSkin.value());
            buf.writeUtf(playerWithSkin.signature());
        });
    }
}
