package com.clefal.teams.modules.internal.propertyhandler;

import com.clefal.teams.event.server.ServerGatherPropertyEvent;

public interface IPropertyServerHandler {
    void onGather(ServerGatherPropertyEvent event);


}
