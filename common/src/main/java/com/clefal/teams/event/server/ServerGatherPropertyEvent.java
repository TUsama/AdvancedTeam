package com.clefal.teams.event.server;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.teams.client.core.IProperty;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class ServerGatherPropertyEvent extends ServerEvent {
    public final ServerPlayer player;
    public List<Tuple2<IProperty, PropertyConsumer>> gather = new ArrayList<>();

    public ServerGatherPropertyEvent(ServerPlayer player) {
        this.player = player;
    }

    public void addProperty(IProperty property, PropertyConsumer consumer){
        //System.out.println("add " + property);
        this.gather.add(Tuple.of(property, consumer));
    }

    public interface PropertyConsumer extends BiConsumer<IProperty, CompoundTag> {
    }
}
