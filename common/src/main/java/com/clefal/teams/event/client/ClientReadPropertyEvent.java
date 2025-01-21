package com.clefal.teams.event.client;

import com.clefal.teams.client.core.IRenderableProperty;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientReadPropertyEvent extends ClientEvent {
    public List<String> properties;
    public final CompoundTag tag;
    private List<IRenderableProperty> results = new ArrayList<>();

    public ClientReadPropertyEvent(CompoundTag tag, List<String> properties) {
        this.tag = tag;
        this.properties = properties;
    }

    public IRenderableProperty[] getResults() {
        return results.toArray(IRenderableProperty[]::new);
    }

    public void addResult(IRenderableProperty property) {
        this.results.add(property);
    }
}
