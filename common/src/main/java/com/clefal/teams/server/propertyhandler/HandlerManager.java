package com.clefal.teams.server.propertyhandler;

import com.clefal.teams.server.propertyhandler.vanilla.VanillaPropertyClientHandler;
import com.clefal.teams.server.propertyhandler.vanilla.VanillaPropertyServerHandler;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager {
    public static HandlerManager INSTANCE = new HandlerManager();
    private final List<IPropertyServerHandler> serverHandlers = new ArrayList<>();
    private final List<IPropertyClientHandler> clientHandlers = new ArrayList<>();

    static {
        INSTANCE.registerHandlerPair(VanillaPropertyServerHandler.INSTANCE, VanillaPropertyClientHandler.INSTANCE);
    }


    public void registerHandlerPair(IPropertyServerHandler handler1, IPropertyClientHandler handler2){
        serverHandlers.add(handler1);
        clientHandlers.add(handler2);
    }

    public List<IPropertyServerHandler> getServerHandlers(){
        return ImmutableList.copyOf(this.serverHandlers);
    }

    public List<IPropertyClientHandler> getClientHandlers(){
        return ImmutableList.copyOf(this.clientHandlers);
    }
}
