package com.clefal.teams.server.handlers;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.Event;

public interface IEventListener<T extends Event> {

    void onListening(T event);
}
