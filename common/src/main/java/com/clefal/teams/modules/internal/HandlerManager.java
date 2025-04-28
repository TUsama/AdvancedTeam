package com.clefal.teams.modules.internal;

import com.clefal.nirvana_lib.utils.SideUtils;
import com.clefal.teams.modules.internal.effect.handlers.VanillaPotionEffectPropertyClientHandler;
import com.clefal.teams.modules.internal.effect.handlers.VanillaPotionEffectPropertyServerHandler;
import com.clefal.teams.modules.internal.propertyhandler.IPropertyClientHandler;
import com.clefal.teams.modules.internal.propertyhandler.IPropertyServerHandler;
import com.clefal.teams.modules.internal.propertyhandler.vanilla.VanillaPropertyClientHandler;
import com.clefal.teams.modules.internal.propertyhandler.vanilla.VanillaPropertyServerHandler;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager {
    public static HandlerManager INSTANCE = new HandlerManager();
    private final List<IPropertyServerHandler> serverHandlers = new ArrayList<>();
    private final List<IPropertyClientHandler> clientHandlers = new ArrayList<>();

    static {
        INSTANCE.registerHandlerPair(VanillaPropertyServerHandler.INSTANCE, VanillaPropertyClientHandler.INSTANCE);
        INSTANCE.registerHandlerPair(VanillaPotionEffectPropertyServerHandler.INSTANCE, VanillaPotionEffectPropertyClientHandler.INSTANCE);
    }


    public void registerHandlerPair(IPropertyServerHandler handler1, IPropertyClientHandler handler2){
        registerAtServer(handler1);
        registerAtClient(handler2);
    }

    public void registerAtServer(IPropertyServerHandler handler1){
        serverHandlers.add(handler1);
    }
    public void registerAtClient(IPropertyClientHandler handler2){
        if (SideUtils.isClient()) {
            clientHandlers.add(handler2);
        }
    }

    public List<IPropertyServerHandler> getServerHandlers(){
        return ImmutableList.copyOf(this.serverHandlers);
    }

    public List<IPropertyClientHandler> getClientHandlers(){
        return ImmutableList.copyOf(this.clientHandlers);
    }
}
