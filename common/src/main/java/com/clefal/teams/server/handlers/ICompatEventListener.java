package com.clefal.teams.server.handlers;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.Event;
import com.clefal.teams.platform.Platform;
import com.clefal.teams.platform.Services;

public interface ICompatEventListener<T extends Event> extends IEventListener<T> {

    String getCompatModID();
    default boolean shouldEnable(){
        return Services.PLATFORM.isModLoaded(getCompatModID());
    }

    void tryEnable();
}
