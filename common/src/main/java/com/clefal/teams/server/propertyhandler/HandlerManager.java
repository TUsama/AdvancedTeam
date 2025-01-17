package com.clefal.teams.server.propertyhandler;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager {
    private static final List<IPropertyHandler> handlers = new ArrayList<>();

    static {
        handlers.add(VanillaPropertyHandler.INSTANCE);
    }


    public static void registerHandler(IPropertyHandler handler){
        handlers.add(handler);
    }
}
