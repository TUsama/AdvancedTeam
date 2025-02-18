package com.clefal.teams.server;

import com.clefal.teams.config.ATServerConfig;
import com.google.common.base.Objects;

import java.util.UUID;

public class Invitation extends ExpirableObject{
    public final String teamName;

    public Invitation(String teamName) {
        this.teamName = teamName;

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
