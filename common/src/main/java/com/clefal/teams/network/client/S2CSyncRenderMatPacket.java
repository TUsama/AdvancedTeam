package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;
import com.clefal.teams.client.core.ClientRenderPersistentData;
import net.minecraft.network.FriendlyByteBuf;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.clefal.nirvana_lib.relocated.io.vavr.API.$;
import static com.clefal.nirvana_lib.relocated.io.vavr.API.Case;

public class S2CSyncRenderMatPacket implements S2CModPacket<S2CSyncRenderMatPacket> {

    public enum Action {
        ADD(Collection::add),
        REMOVE((collection, string) -> collection.removeIf(x -> x.equals(string)));

        public final BiConsumer<Collection<String>, String> action;

        Action(BiConsumer<Collection<String>, String> action) {
            this.action = action;
        }
    }

    public enum Type{
        INVITATION(() -> ClientRenderPersistentData.getInstance().invitations),
        APPLICATION(() -> ClientRenderPersistentData.getInstance().applications);

        public final Supplier<List<String>> target;

        Type(Supplier<List<String>> target) {
            this.target = target;
        }
    }

    String name;
    Action action;
    Type type;
    public S2CSyncRenderMatPacket(String name, Action action, Type type) {
        this.name = name;
        this.action = action;
        this.type = type;
    }

    public S2CSyncRenderMatPacket() {

    }

    @Override
    public void handleClient() {
        action.action.accept(type.target.get(), name);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeUtf(name, 100);
        to.writeByte(action.ordinal());
        to.writeByte(type.ordinal());
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        this.name = friendlyByteBuf.readUtf(100);
        this.action = Action.values()[friendlyByteBuf.readByte()];
        this.type = Type.values()[friendlyByteBuf.readByte()];
    }

    @Override
    public Class<S2CSyncRenderMatPacket> getSelfClass() {
        return S2CSyncRenderMatPacket.class;
    }
}
