package com.clefal.teams.event.server;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.HashSet;
import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ServerGatherPropertyEvent extends ServerEvent {
    public final ServerPlayer player;
    public List<Tuple2<IProperty, PropertyConsumer>> gather = new ArrayList<>();
    public final HashSet<String> keys;

    public ServerGatherPropertyEvent(ServerPlayer player, HashSet<String> keys) {
        this.player = player;
        this.keys = keys;
    }

    public boolean needUpdate(String key){
        return keys.contains(key);
    }

    public void addProperty(IProperty property, PropertyConsumer consumer){
        //System.out.println("add " + property);
        this.gather.add(Tuple.of(property, consumer));
    }

    public static class ServerGatherAllPropertyEvent extends ServerGatherPropertyEvent{

        public ServerGatherAllPropertyEvent(ServerPlayer player) {
            super(player, HashSet.empty());
        }

        @Override
        public boolean needUpdate(String key) {
            return true;
        }
    }

    public interface PropertyConsumer extends BiConsumer<IProperty, CompoundTag> {
    }
}
