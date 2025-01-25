package com.clefal.teams.compat;

import com.clefal.teams.platform.Services;

public interface ICompatModule {
    String getModID();
    void whenEnable();
    default void tryEnable(){
        if (shouldBeEnable()){
            whenEnable();
        }
    }

    default boolean shouldBeEnable(){
        return Services.PLATFORM.isModLoaded(getModID());
    }
}
