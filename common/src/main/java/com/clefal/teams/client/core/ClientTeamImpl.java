package com.clefal.teams.client.core;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.client.gui.screens.hasteam.HasTeamScreen;
import com.clefal.teams.client.gui.screens.noteam.NoTeamScreen;
import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

class ClientTeamImpl implements ClientTeam {

    private Minecraft client = Minecraft.getInstance();
    private UUID leader;
    private Map<UUID, Teammate> teammates = new TreeMap<>(new Comparator<UUID>() {
        @Override
        public int compare(UUID uuid, UUID t1) {
            if (uuid.equals(leader)) {
                return -1;
            } else if (t1.equals(leader)) {
                return 1;
            } else {
                return uuid.compareTo(t1);
            }
        }

    });
    private boolean initialized = false;
    private String name = "";
    private boolean hasPermission;
    private boolean allowEveryoneInvite;

    ClientTeamImpl() {
    }

    @Override
    public void init(String name, UUID leader) {
        if (this.initialized) {
            throw new IllegalArgumentException("Cannot initialize already initialized team. Did you clear it first?");
        }
        this.name = name;
        this.leader = leader;
        this.initialized = true;
    }

    @Override
    public void changeLeader(UUID leader) {
        this.leader = leader;
    }

    @Override
    public boolean isLeader(UUID id) {
        return leader.equals(id);
    }

    @Override
    public void updatePermission(boolean hasPermission) {
        this.hasPermission = hasPermission;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean canInvite() {
        return allowEveryoneInvite || hasPermission;
    }

    @Override
    public boolean allowEveryoneInvite() {
        return allowEveryoneInvite;
    }

    @Override
    public void setCanInvite(boolean canInvite) {
        this.allowEveryoneInvite = canInvite;
    }

    @Override
    public boolean hasPermissions() {
        return hasPermission;
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
        if (client.screen instanceof HasTeamScreen screen) {
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
            // it means that the update is earlier than the client joins the world.
            // this should be ignored
            if (teammates.get(client.player.getUUID()) != null)
                AdvancedTeam.LOGGER.warn("Tried updating player with UUID " + player + ", but they are not in this client team");
        }
    }

    @Override
    public void removePlayer(UUID player) {
        teammates.remove(player);
        // Refresh TeamsMainScreen if open, or close it if we were kicked
        if (client.screen instanceof HasTeamScreen screen) {
            if (teammates.isEmpty() || player.equals(client.player.getUUID())) {
                client.setScreen(screen.parent);
            } else {
                screen.refresh();
            }
        } else if (client.screen instanceof NoTeamScreen screen) {
            screen.refresh();
        }
    }


    @Override
    public void reset() {
        teammates.clear();
        name = "";
        leader = null;
        initialized = false;
        // If in TeamsScreen, go to lonely screen
        if (client.screen instanceof TeamsScreen) {
            client.setScreen(new NoTeamScreen(null));
        }
    }

}
