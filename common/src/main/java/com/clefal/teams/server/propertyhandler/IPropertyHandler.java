package com.clefal.teams.server.propertyhandler;

import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import net.minecraft.server.level.ServerPlayer;

public interface IPropertyHandler {
    void onGather(ServerGatherPropertyEvent event);
    void onRead(ClientReadPropertyEvent event);
}
