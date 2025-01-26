package com.clefal.teams.server.propertyhandler;

import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;

public interface IPropertyServerHandler {
    void onGather(ServerGatherPropertyEvent event);


}
