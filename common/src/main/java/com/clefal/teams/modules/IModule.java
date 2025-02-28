package com.clefal.teams.modules;

import com.clefal.teams.platform.Services;

public interface IModule {

    void whenEnable();
    default void tryEnable(){
        if (shouldBeEnable()){
            whenEnable();
        }
    }

    boolean shouldBeEnable();
}
