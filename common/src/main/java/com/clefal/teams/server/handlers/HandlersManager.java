package com.clefal.teams.server.handlers;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;

public class HandlersManager {
    private final List<IEventListener<?>> allHandlers;
    public final HandlersManager INSTANCE = new HandlersManager();


    public HandlersManager() {
        this.allHandlers = List.of();
        initInternal();
        initCompat();
    }


    private void initInternal(){
        this.allHandlers.push(

        );
    }

    private void initCompat(){

    }
}
