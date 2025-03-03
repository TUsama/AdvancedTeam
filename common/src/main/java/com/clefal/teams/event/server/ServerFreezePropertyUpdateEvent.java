package com.clefal.teams.event.server;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.HashSet;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class ServerFreezePropertyUpdateEvent extends ServerEvent{
    private final Set<String> keys;

    public ServerFreezePropertyUpdateEvent(Set<String> keys) {
        this.keys = keys;
    }

    public void removeKey(String key){
        keys.remove(key);
    }

    /**
     * @return an immutable HashSet with results.
     */
    public HashSet<String> getResult(){
        return HashSet.ofAll(keys);
    }
}
