package com.clefal.teams.event.client;

import com.clefal.teams.server.propertyhandler.IPropertyHandler;

import java.util.List;

public class ClientGatherHandlerEvent extends ClientEvent{
    public List<IPropertyHandler> handlers;
}
