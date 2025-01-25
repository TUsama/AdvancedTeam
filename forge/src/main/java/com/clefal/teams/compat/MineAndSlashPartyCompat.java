package com.clefal.teams.compat;

public class MineAndSlashPartyCompat implements ISubCompatModule<MineAndSlashCompatModule>{
    public static final MineAndSlashPartyCompat INSTANCE = new MineAndSlashPartyCompat();
    @Override
    public MineAndSlashCompatModule getMainModule() {
        return MineAndSlashCompatModule.INSTANCE;
    }

    @Override
    public void whenEnable() {

    }


}
