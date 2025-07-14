package com.clefal.teams.modules.compat;

import com.clefal.nirvana_lib.utils.ModUtils;
import com.clefal.teams.modules.IModule;

public interface ICompatModule extends IModule {
    String getModID();

    default boolean shouldBeEnable(){
        return ModUtils.isModLoaded(getModID());
    }
}
