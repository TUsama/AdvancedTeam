package com.clefal.teams.event.client;

import com.clefal.nirvana_lib.relocated.io.vavr.Function1;
import com.clefal.nirvana_lib.relocated.io.vavr.Lazy;
import com.clefal.teams.client.core.IProperty;
import com.clefal.teams.client.core.property.impl.PropertyRenderer;
import com.google.common.collect.ImmutableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientRegisterPropertyRendererEvent extends ClientEvent{
    private final Map<String, Function1<IProperty, PropertyRenderer<? extends IProperty>>> gather = new HashMap<>();

    public <T extends IProperty> void register(String identifier, Function1<IProperty, PropertyRenderer<? extends IProperty>> renderer){
        gather.put(identifier, renderer.memoized());
    }

    public Map<String, Function1<IProperty, PropertyRenderer<?>>> getResult(){
        return ImmutableMap.copyOf(gather);
    }
}
