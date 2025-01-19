package com.clefal.teams.server.propertyhandler;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager {
    public static HandlerManager INSTANCE = new HandlerManager();
    private final List<IPropertyHandler> handlers = new ArrayList<>();

    static {
        INSTANCE.registerHandler(VanillaPropertyHandler.INSTANCE);
    }


    public void registerHandler(IPropertyHandler handler){
        handlers.add(handler);
    }

    public List<IPropertyHandler> getHandlers(){
        return ImmutableList.copyOf(this.handlers);
    }
}
