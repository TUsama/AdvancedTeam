package com.clefal.teams.modules;


public interface IModule {

    void whenEnable();
    default void tryEnable(){
        if (shouldBeEnable()){
            whenEnable();
        }
    }

    boolean shouldBeEnable();
}
