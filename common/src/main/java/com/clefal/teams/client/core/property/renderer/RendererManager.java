package com.clefal.teams.client.core.property.renderer;

import com.clefal.nirvana_lib.relocated.io.vavr.Function1;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import com.clefal.teams.client.core.property.impl.PropertyRenderer;
import com.clefal.teams.event.client.ClientRegisterPropertyRendererEvent;

import java.util.HashMap;
import java.util.Map;

public class RendererManager {

    private static final RendererManager INSTANCE = new RendererManager();

    private final Map<String, Function1<IProperty, PropertyRenderer<?>>> map = new HashMap<>();

    public static void init(){
    }

    static {
        ClientRegisterPropertyRendererEvent clientRegisterPropertyRendererEvent = new ClientRegisterPropertyRendererEvent();
        Map<String, Function1<IProperty, PropertyRenderer<?>>> result = AdvancedTeam.post(clientRegisterPropertyRendererEvent).getResult();
        INSTANCE.map.putAll(result);
    }

    public static <T extends IProperty> PropertyRenderer<T> getRenderer(IProperty property){
        return ((PropertyRenderer<T>) INSTANCE.map.get(property.getIdentifier()).apply(property));
    }
}
