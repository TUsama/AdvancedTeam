package com.clefal.teams.event.client;

import com.clefal.nirvana_lib.relocated.io.vavr.Function1;
import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import com.clefal.teams.client.core.property.impl.PropertyRenderer;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class ClientRegisterPropertyRendererEvent extends ClientEvent{
    private final Map<String, Function1<IProperty, PropertyRenderer<? extends IProperty>>> gather = new HashMap<>();

    public void register(String identifier, Function1<IProperty, PropertyRenderer<? extends IProperty>> renderer){
        gather.put(identifier, renderer);
    }

    public Map<String, Function1<IProperty, PropertyRenderer<?>>> getResult(){
        return ImmutableMap.copyOf(gather);
    }
}
