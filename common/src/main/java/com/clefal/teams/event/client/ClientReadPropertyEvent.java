package com.clefal.teams.event.client;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;
import com.clefal.teams.client.core.IRenderableProperty;
import net.minecraft.nbt.CompoundTag;

public class ClientReadPropertyEvent extends ClientEvent {
    public List<String> properties;
    public final CompoundTag tag;
    private List<IRenderableProperty> results = List.of();

    public ClientReadPropertyEvent(CompoundTag tag, List<String> properties) {
        this.tag = tag;
        this.properties = properties;
    }

    public IRenderableProperty[] getResults() {
        return results.reverse().toJavaArray(IRenderableProperty[]::new);
    }

    public void addResult(IRenderableProperty property) {
        this.results.prepend(property);
    }
}
