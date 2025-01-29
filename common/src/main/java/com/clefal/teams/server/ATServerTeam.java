package com.clefal.teams.server;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.event.server.ServerOnPlayerOnlineEvent;
import com.clefal.teams.event.server.ServerPromoteEvent;
import com.clefal.teams.network.client.*;
import com.mojang.authlib.GameProfile;
import com.clefal.teams.mixin.AdvancementAccessor;
import com.clefal.teams.platform.Services;
import lombok.Getter;
import lombok.val;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;


public class ATServerTeam extends Team {

    public final String name;
    private final ATServerTeamData teamData;
    @Getter
    private UUID leader;
    private Set<UUID> players;
    private Map<UUID, ServerPlayer> onlinePlayers;
    private final Set<Advancement> advancements = new LinkedHashSet<>();
    private PlayerTeam scoreboardTeam;

    private ATServerTeam(Scoreboard scoreboard, String name, ATServerTeamData teamData, UUID leader) {
        this.name = name;
        this.teamData = teamData;
        this.leader = leader;
        players = new HashSet<>();
        onlinePlayers = new LinkedHashMap<>();
        scoreboardTeam = scoreboard.getPlayerTeam(name);
        if (scoreboardTeam == null) {
            scoreboardTeam = scoreboard.addPlayerTeam(name);
        }
    }

    public void promote(ServerPlayer player){
        this.leader = player.getUUID();
        Services.PLATFORM.sendToClients(new S2CPermissionChangePacket(this.leader), this.onlinePlayers.values());
        AdvancedTeam.post(new ServerPromoteEvent(player));
    }

    public boolean playerHasPermissions(ServerPlayer player) {
        return getLeader().equals(player.getUUID()) || player.hasPermissions(2);
    }
    public Collection<ServerPlayer> getOnlinePlayers() {
        return onlinePlayers.values();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean hasPlayer(ServerPlayer player) {
        return hasPlayer(player.getUUID());
    }

    public boolean hasPlayer(UUID player) {
        return players.contains(player);
    }

    public void addPlayer(ServerPlayer player) {
        addPlayer(player.getUUID());
    }

    public void removePlayer(ServerPlayer player) {
        removePlayer(player.getUUID());
    }

    public void clear() {
        var playersCopy = new ArrayList<>(players);
        playersCopy.forEach(player -> removePlayer(player));
        advancements.clear();
    }

    public void addAdvancement(Advancement advancement) {
        advancements.add(advancement);
    }

    public void onPlayerOnline(ServerPlayer player, boolean sendPackets) {
        onlinePlayers.put(player.getUUID(), player);
        ((IHasTeam) player).setTeam(this);
        // Packets
        if (sendPackets) {
            Services.PLATFORM.sendToClient(new S2CTeamInitPacket(name, leader), player);
            if (onlinePlayers.size() == 1) {
                var players = teamData.serverLevel.getServer().getPlayerList().getPlayers();
                Services.PLATFORM.sendToClients(new S2CTeamDataUpdatePacket(S2CTeamDataUpdatePacket.Type.ONLINE, name), players);
            }
            var players = getOnlinePlayers();
            Services.PLATFORM.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.ADD), players);
            for (var teammate : players) {
                Services.PLATFORM.sendToClient(new S2CTeamPlayerDataPacket(teammate, S2CTeamPlayerDataPacket.Type.ADD), player);
            }
        }
        // Advancement Sync
        for (Advancement advancement : advancements) {
            AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            for (String criterion : progress.getRemainingCriteria()) {
                player.getAdvancements().award(advancement, criterion);
            }
        }
        AdvancedTeam.post(new ServerOnPlayerOnlineEvent(player));
    }

    public Stream<UUID> getPlayerUuids() {
        return players.stream();
    }

    public void onPlayerOffline(ServerPlayer player, boolean sendPackets) {
        onlinePlayers.remove(player.getUUID());
        // Packets
        if (sendPackets) {
            if (isEmpty()) {
                var players = teamData.serverLevel.getServer().getPlayerList().getPlayers();
                Services.PLATFORM.sendToClients(new S2CTeamDataUpdatePacket(S2CTeamDataUpdatePacket.Type.OFFLINE, name), players);
            }
            var players = getOnlinePlayers();
            Services.PLATFORM.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.REMOVE), players);
        }
    }

    private void addPlayer(UUID player) {
        players.add(player);
        String playerName = getNameFromUUID(player);
        // Scoreboard
        var playerScoreboardTeam = teamData.scoreboard.getPlayersTeam(playerName);
        if (playerScoreboardTeam == null || !playerScoreboardTeam.isAlliedTo(scoreboardTeam)) {
            teamData.scoreboard.addPlayerToTeam(playerName, scoreboardTeam);
        }
        var playerEntity = teamData.serverLevel.getServer().getPlayerList().getPlayer(player);
        if (playerEntity != null) {
            // Packets
            Services.PLATFORM.sendToClient(new S2CTeamUpdatePacket(name, playerName, S2CTeamUpdatePacket.Action.JOINED, true), playerEntity);
            Services.PLATFORM.sendToClients(new S2CTeamUpdatePacket(name, playerName, S2CTeamUpdatePacket.Action.JOINED, false), getOnlinePlayers());
            onPlayerOnline(playerEntity, true);
            // Advancement Sync
            Set<Advancement> advancements = ((AdvancementAccessor) playerEntity.getAdvancements()).getVisibleAdvancements();
            for (Advancement advancement : advancements) {
                if (playerEntity.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    addAdvancement(advancement);
                }
            }
        }
        teamData.setDirty();
    }

    private void removePlayer(UUID player) {
        players.remove(player);
        if (this.leader == player) {
            Iterator<ServerPlayer> iterator = onlinePlayers.values().iterator();
            if (iterator.hasNext()){
                this.promote(iterator.next());

            }
        }
        String playerName = getNameFromUUID(player);
        // Scoreboard
        var playerScoreboardTeam = teamData.scoreboard.getPlayersTeam(playerName);
        if (playerScoreboardTeam != null && playerScoreboardTeam.isAlliedTo(scoreboardTeam)) {
            teamData.scoreboard.removePlayerFromTeam(playerName, scoreboardTeam);
        }
        // Packets
        var playerEntity = teamData.serverLevel.getServer().getPlayerList().getPlayer(player);
        if (playerEntity != null) {
            onPlayerOffline(playerEntity, true);
            Services.PLATFORM.sendToClient(new S2CTeamClearPacket(), playerEntity);
            Services.PLATFORM.sendToClient(new S2CTeamUpdatePacket(name, playerName, S2CTeamUpdatePacket.Action.LEFT, true), playerEntity);
            Services.PLATFORM.sendToClients(new S2CTeamUpdatePacket(name, playerName, S2CTeamUpdatePacket.Action.LEFT, false), getOnlinePlayers());
            ((IHasTeam) playerEntity).setTeam(null);
        }
        teamData.setDirty();
    }

    private String getNameFromUUID(UUID id) {
        return teamData.serverLevel.getServer().getProfileCache().get(id).map(GameProfile::getName).orElseThrow();
    }

    static ATServerTeam fromNBT(CompoundTag compound, ATServerTeamData teamData) {
        ATServerTeam team = new Builder(compound.getString("name"))
                .complete(teamData, compound.getUUID("leader"));
        val vanillaTeam = team.scoreboardTeam;
        vanillaTeam.setColor(ChatFormatting.getByName(compound.getString("colour")));
        vanillaTeam.setCollisionRule(CollisionRule.byName(compound.getString("collision")));
        vanillaTeam.setAllowFriendlyFire(compound.getBoolean("friendlyFire"));
        vanillaTeam.setSeeFriendlyInvisibles(compound.getBoolean("showInvisible"));
        vanillaTeam.setDeathMessageVisibility(Visibility.byName(compound.getString("deathMessages")));
        vanillaTeam.setNameTagVisibility(Visibility.byName(compound.getString("nameTags")));


        ListTag players = compound.getList("players", Tag.TAG_INT_ARRAY);
        for (var elem : players) {
            try {
                UUID uuid = NbtUtils.loadUUID(elem);
                team.addPlayer(uuid);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        ListTag advancements = compound.getList("advancement", Tag.TAG_STRING);
        for (var adv : advancements) {
            ResourceLocation id = ResourceLocation.tryParse(adv.getAsString());
            team.addAdvancement(teamData.serverLevel.getServer().getAdvancements().getAdvancement(id));
        }

        return team;
    }

    CompoundTag toNBT() {
        CompoundTag compound = new CompoundTag();
        compound.putString("name", name);
        compound.putUUID("leader", leader);
        compound.putString("colour", scoreboardTeam.getColor().getName());
        compound.putString("collision", scoreboardTeam.getCollisionRule().name);
        compound.putString("deathMessages", scoreboardTeam.getDeathMessageVisibility().name);
        compound.putString("nameTags", scoreboardTeam.getNameTagVisibility().name);
        compound.putBoolean("friendlyFire", scoreboardTeam.isAllowFriendlyFire());
        compound.putBoolean("showInvisible", scoreboardTeam.canSeeFriendlyInvisibles());

        ListTag playerList = new ListTag();
        for (var player : players) {

            playerList.add(NbtUtils.createUUID(player));
        }
        compound.put("players", playerList);

        ListTag advList = new ListTag();
        for (var advancement : advancements) {
            advList.add(StringTag.valueOf(advancement.getId().toString()));
        }
        compound.put("advancements", advList);

        return compound;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public MutableComponent getFormattedName(Component name) {
        return scoreboardTeam.getFormattedName(name);
    }

    @Override
    public boolean canSeeFriendlyInvisibles() {
        return scoreboardTeam.canSeeFriendlyInvisibles();
    }

    public void setShowFriendlyInvisibles(boolean value) {
        scoreboardTeam.setSeeFriendlyInvisibles(value);
    }

    @Override
    public boolean isAllowFriendlyFire() {
        return scoreboardTeam.isAllowFriendlyFire();
    }

    public void setFriendlyFireAllowed(boolean value) {
        scoreboardTeam.setAllowFriendlyFire(value);
    }

    @Override
    public Visibility getNameTagVisibility() {
        return scoreboardTeam.getNameTagVisibility();
    }

    public void setNameTagVisibilityRule(Visibility value) {
        scoreboardTeam.setNameTagVisibility(value);
    }

    @Override
    public ChatFormatting getColor() {
        return scoreboardTeam.getColor();
    }

    public void setColour(ChatFormatting colour) {
        scoreboardTeam.setColor(colour);
    }

    @Override
    public Collection<String> getPlayers() {
        return scoreboardTeam.getPlayers();
    }

    @Override
    public Visibility getDeathMessageVisibility() {
        return scoreboardTeam.getDeathMessageVisibility();
    }

    public void setDeathMessageVisibilityRule(Visibility value) {
        scoreboardTeam.setDeathMessageVisibility(value);
    }

    @Override
    public CollisionRule getCollisionRule() {
        return scoreboardTeam.getCollisionRule();
    }

    public void setCollisionRule(CollisionRule value) {
        scoreboardTeam.setCollisionRule(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ATServerTeam team && Objects.equals(team.getName(), this.name);
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }



    public static class Builder {

        private final String name;

        public Builder(String name) {
            this.name = name;
        }

        public ATServerTeam complete(ATServerTeamData teamData, UUID leader) {
            ATServerTeam team = new ATServerTeam(teamData.scoreboard, name, teamData, leader);
            return team;
        }
    }

    public PlayerTeam getScoreboardTeam() {
        return scoreboardTeam;
    }
}
