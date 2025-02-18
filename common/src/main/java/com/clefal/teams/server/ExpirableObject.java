package com.clefal.teams.server;

import com.clefal.teams.config.ATServerConfig;
import com.clefal.teams.utils.expirable.IExpirable;

public abstract class ExpirableObject implements IExpirable {
    private int lifetime;
    private static int maxLifeTime;


    public ExpirableObject() {
        lifetime = 0;
        if (maxLifeTime != ATServerConfig.config.invitationAndApplicationExpireTick.get()){
            maxLifeTime = ATServerConfig.config.invitationAndApplicationExpireTick.get();
        }
    }

    public boolean update(){
        lifetime += 1;
        return lifetime >= maxLifeTime;
    }
}
