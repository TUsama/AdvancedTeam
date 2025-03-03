package com.clefal.teams.event.client;

import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class ClientReadPropertyEvent extends ClientEvent {
    public List<String> properties;
    public final CompoundTag tag;
    private List<IProperty> results = new ArrayList<>();

    public ClientReadPropertyEvent(CompoundTag tag, List<String> properties) {
        this.tag = tag;
        this.properties = properties;
    }

    public IProperty[] getResults() {
        return results.toArray(IProperty[]::new);
    }

    public void addResult(IProperty property) {
        this.results.add(property);
    }
}
