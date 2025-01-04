package com.clefal.teams.platform;

public enum PhysicalSide {
    CLIENT,SERVER;

    public boolean isClient() {
        return this == CLIENT;
    }
}
