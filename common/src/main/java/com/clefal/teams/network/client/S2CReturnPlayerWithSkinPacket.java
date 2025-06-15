package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;
import com.clefal.teams.client.gui.util.PlayerWithSkin;
import com.clefal.teams.utils.ClientHelper;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;

public class S2CReturnPlayerWithSkinPacket implements S2CModPacket<S2CReturnPlayerWithSkinPacket> {

    public enum Usage{
        INVITATION,
        REQUEST
    }

    List<PlayerWithSkin> playersName;
    Usage usage;

    public S2CReturnPlayerWithSkinPacket(List<PlayerWithSkin> playersName, Usage usage) {
        this.playersName = playersName;
        this.usage = usage;
    }

    public S2CReturnPlayerWithSkinPacket() {
    }

    @Override
    public void handleClient() {
        switch (usage){
            case INVITATION -> ClientHelper.tryUpdateInvitationScreenEntryList(playersName);
            case REQUEST -> {
                ClientHelper.tryUpdateApplicationScreenEntryList(playersName);
            }
        }

    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeCollection(playersName.toJavaList(), (buf, playerWithSkin) -> {
            buf.writeUtf(playerWithSkin.name());
            buf.writeUUID(playerWithSkin.uuid());
            buf.writeUtf(playerWithSkin.value());
            buf.writeUtf(playerWithSkin.signature());
        });
        to.writeEnum(usage);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        java.util.List<PlayerWithSkin> objects = friendlyByteBuf.readCollection(x -> new ArrayList<>(), buf -> new PlayerWithSkin(
                buf.readUtf(), buf.readUUID(), buf.readUtf(), buf.readUtf()
        ));
        playersName = List.ofAll(objects);
        usage = friendlyByteBuf.readEnum(Usage.class);
    }

    @Override
    public Class<S2CReturnPlayerWithSkinPacket> getSelfClass() {
        return S2CReturnPlayerWithSkinPacket.class;
    }
}
