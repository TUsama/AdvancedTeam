package com.clefal.teams.modules.compat;

import com.clefal.teams.modules.IModule;
import com.clefal.teams.platform.Services;

public interface ICompatModule extends IModule {
    String getModID();

    default boolean shouldBeEnable(){
        return Services.PLATFORM.isModLoaded(getModID());
    }
}
