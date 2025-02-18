package com.clefal.teams.client.core;


import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;

import java.util.LinkedHashSet;
import java.util.Set;

public class ClientTeamData {

    public static ClientTeamData INSTANCE = new ClientTeamData();

    private final Set<String> teams;
    private final Set<String> onlineTeams;
    private final Set<String> publicTeams;

    private ClientTeamData() {
        teams = new LinkedHashSet<>();
        onlineTeams = new LinkedHashSet<>();
        this.publicTeams = new LinkedHashSet<>();
    }

    public List<String> getTeams() {
        return List.ofAll(teams);
    }

    public List<String> getOnlineTeams() {
        return List.ofAll(onlineTeams);
    }

    public void addPublicTeam(String team){
        this.publicTeams.add(team);
    }

    public void removePublicTeam(String team){
        this.publicTeams.remove(team);
    }

    public boolean isPublicTeam(String team){
        return  publicTeams.contains(team);
    }

    public void addTeam(String team) {
        teams.add(team);
    }

    public void removeTeam(String team) {
        teams.remove(team);
        teamOffline(team);
    }

    public boolean containsTeam(String team) {
        return teams.contains(team);
    }

    public void teamOnline(String team) {
        onlineTeams.add(team);
    }

    public void teamOffline(String team) {
        onlineTeams.remove(team);
    }

    public boolean containsOnlineTeam(String team) {
        return onlineTeams.contains(team);
    }

    public void clear() {
        teams.clear();
        onlineTeams.clear();
    }

}
