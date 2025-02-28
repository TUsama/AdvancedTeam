package com.clefal.teams.modules.internal.effect;

import com.clefal.teams.modules.internal.IInternalModule;

public class VanillaPotionEffectModule implements IInternalModule {

    public static boolean isEnable = false;

    @Override
    public void whenEnable() {
        isEnable = true;
    }

    @Override
    public boolean shouldBeEnable() {
        return false;
    }
}
