package com.clefal.teams.client.core;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.gui.menu.TeamsLonelyScreen;
import com.clefal.teams.client.gui.menu.TeamsMainScreen;
import com.clefal.teams.client.gui.menu.TeamsScreen;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

class ClientTeamImpl implements ClientTeam {

    private Minecraft client = Minecraft.getInstance();
    private Map<UUID, Teammate> teammates = new HashMap<>();
    private boolean initialized = false;
    private String name = "";
    private boolean hasPerms = false;

    ClientTeamImpl() {
    }

    @Override
    public void init(String name, boolean hasPermissions) {
        if (this.initialized) {
            throw new IllegalArgumentException("Cannot initialize already initialized team. Did you clear it first?");
        }
        this.name = name;
        this.hasPerms = hasPermissions;
        this.initialized = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean hasPermissions() {
        return hasPerms;
    }

    @Override
    public boolean isInTeam() {
        return initialized;
    }

    @Override
    public boolean isTeamEmpty() {
        return teammates.size() == 0 || (teammates.size() == 1 && teammates.get(client.player.getUUID()) != null);
    }

    @Override
    public List<Teammate> getTeammates() {
        return ImmutableList.copyOf(teammates.values());
    }

    public boolean hasPlayer(UUID player) {
        return teammates.containsKey(player);
    }

    @Override
    public void addPlayer(UUID player, String name, ResourceLocation skin, IProperty... others) {
        teammates.put(player, new Teammate(player, name, skin, others));
        // Refresh TeamsMainScreen if open
        if (client.screen instanceof TeamsMainScreen screen) {
            screen.refresh();
        } // Close TeamsScreens if we join a team
        else if (player.equals(client.player.getUUID()) && client.screen instanceof TeamsScreen) {
            client.setScreen(null);
        }
    }

    @Override
    public void updatePlayer(UUID player, IProperty... properties) {
        var teammate = teammates.get(player);
        if (teammate != null) {
            for (IProperty property : properties) {
                teammate.addProperty(property);
            }

        } else {
            AdvancedTeam.LOGGER.warn("Tried updating player with UUID " + player + "but they are not in this clients team");
        }
    }

    @Override
    public void removePlayer(UUID player) {
        teammates.remove(player);
        // Refresh TeamsMainScreen if open, or close it if we were kicked
        if (client.screen instanceof TeamsMainScreen screen) {
            if (teammates.isEmpty() || player.equals(client.player.getUUID())) {
                client.setScreen(screen.parent);
            } else {
                screen.refresh();
            }
        } else if (client.screen instanceof TeamsLonelyScreen screen) {
            screen.refresh();
        }
    }


    @Override
    public void reset() {
        teammates.clear();
        name = "";
        hasPerms = false;
        initialized = false;
        // If in TeamsScreen, go to lonely screen
        if (client.screen instanceof TeamsScreen) {
            client.setScreen(new TeamsLonelyScreen(null));
        }
    }

}
