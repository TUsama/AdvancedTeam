package com.clefal.teams.compat;

public interface ISubCompatModule<T extends ICompatModule> extends ICompatModule{
    T getMainModule();

    @Override
    default String getModID(){
        return getMainModule().getModID();
    }

    @Override
    default boolean shouldBeEnable() {
        return ICompatModule.super.shouldBeEnable();
    }
}
