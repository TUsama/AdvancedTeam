package com.clefal.teams.server;

import com.clefal.teams.config.ATServerConfig;
import com.google.common.base.Objects;

import java.util.UUID;

public class Invitation {
    public final String teamName;
    private int lifetime;
    private static int maxLifeTime;

    public Invitation(String teamName) {
        this.teamName = teamName;
        lifetime = 0;
        if (maxLifeTime != ATServerConfig.config.invitationExpireTime.get()){
            maxLifeTime = ATServerConfig.config.invitationExpireTime.get();
        }
    }

    public boolean update(){
        lifetime++;
        return lifetime >= maxLifeTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Invitation that)) return false;
        return Objects.equal(teamName, that.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(teamName);
    }
}
