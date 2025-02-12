package com.clefal.teams.network;
//todo need refactor if more and more things need to be transferred.
public record TeamConfig(boolean isPublic) {

    public static TeamConfig fromString(String str) {
        String[] split = str.split("\\+");
        return new TeamConfig(Boolean.getBoolean(split[0]));
    }


    @Override
    public String toString() {
        return "" + isPublic;
    }
}
