package com.clefal.teams.event.client;

import com.clefal.teams.server.propertyhandler.IPropertyServerHandler;

import java.util.List;

public class ClientGatherHandlerEvent extends ClientEvent{
    public List<IPropertyServerHandler> handlers;
}
