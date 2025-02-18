package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.relocated.io.vavr.API;
import com.clefal.teams.client.core.ClientRenderPersistentData;
import net.minecraft.network.FriendlyByteBuf;

import static com.clefal.nirvana_lib.relocated.io.vavr.API.$;
import static com.clefal.nirvana_lib.relocated.io.vavr.API.Case;
import static com.clefal.nirvana_lib.relocated.io.vavr.Predicates.is;

public class S2CInvitationPacket implements S2CModPacket{

    public enum Type{
        ADD,
        REMOVE
    }

    String teamName;
    Type type;

    public S2CInvitationPacket(String name, Type type) {
        this.teamName = name;
        this.type = type;
    }

    public S2CInvitationPacket(FriendlyByteBuf byteBuf) {
        this.teamName = byteBuf.readUtf(100);
        this.type = Type.values()[byteBuf.readByte()];
    }

    @Override
    public void handleClient() {
        API.Match(type).of(
                Case($(is(Type.REMOVE)), ClientRenderPersistentData.getInstance().invitations.removeIf(x -> x.equals(teamName))),
                Case($(), ClientRenderPersistentData.getInstance().invitations.add(teamName))
        );

    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(teamName, 100);
        to.writeByte(type.ordinal());
    }
}
