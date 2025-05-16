package com.clefal.teams.server;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.config.ATServerConfig;
import com.clefal.teams.event.server.ServerOnPlayerOnlineEvent;
import com.clefal.teams.event.server.ServerPromoteEvent;
import com.clefal.teams.mixin.AdvancementAccessor;
import com.clefal.teams.network.client.*;
import com.clefal.teams.network.client.config.S2CTeamConfigBooleanPacket;
import com.clefal.teams.server.team.*;
import com.google.common.base.Objects;
import com.mojang.authlib.GameProfile;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Scoreboard;

import java.util.*;

@RequiredArgsConstructor
public class ATServerTeam implements IPostProcess {
    @Getter
    private final ATServerTeamCore core;
    @Getter
    private final ATServerTeamMembership membership;
    @Getter
    private final ATServerTeamConfig config;
    private final ATServerTeamAttachments attachments;
    private ATServerTeamData teamData;


    public static Codec<ATServerTeam> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ATServerTeamCore.CODEC.fieldOf("core").forGetter(x -> x.core),
                    ATServerTeamMembership.CODEC.fieldOf("member").forGetter(x -> x.membership),
                    ATServerTeamConfig.CODEC.fieldOf("config").forGetter(x -> x.config),
                    ATServerTeamAttachments.CODEC.fieldOf("attachments").forGetter(x -> x.attachments)

            ).apply(instance, ATServerTeam::new)
    );


    private ATServerTeam(String name, ATServerTeamData teamData, UUID leader) {
        UUID uuid = UUID.randomUUID();
        this.core = new ATServerTeamCore(name, uuid);
        this.config = new ATServerTeamConfig(false, false, false, false);
        this.membership = new ATServerTeamMembership(leader);
        this.teamData = teamData;
        this.attachments = new ATServerTeamAttachments(new VanillaTeamAttachment(name + uuid), new AdvancementSyncAttachment());
    }

    public void announceConfigChangeToClient() {
        List<ServerPlayer> players1 = this.teamData.serverLevel.getServer().getPlayerList().getPlayers();
        NetworkUtils.sendToClients(new S2CTeamConfigBooleanPacket.Public(core.name(), config.isPublic), players1);
        NetworkUtils.sendToClients(new S2CTeamConfigBooleanPacket.EveryoneCanInvite(core.name(), config.allowEveryoneInvite), membership.getOnlineMembers().values());
    }

    public void promote(ServerPlayer player) {
        ServerPlayer oldLeader = getOnlinePlayers().find(x -> x.getUUID().equals(membership.getLeader())).get();
        if (oldLeader == null) throw new NullPointerException("can't find old leader when try to promote!");
        membership.setLeader(player.getUUID());
        membership.forAllOnlineMembers(x -> NetworkUtils.sendToClient(new S2CPermissionUpdatePacket(this.playerHasPermissions(x), membership.getLeader()), x));
        AdvancedTeam.post(new ServerPromoteEvent(oldLeader, player, this));
    }

    public void addApplication(Application application) {
        ServerPlayer player = teamData.serverLevel.getServer().getPlayerList().getPlayer(application.applicant);
        if (player == null) throw new NullPointerException("can't find player with UUID " + application.applicant + " when try to add application.");
        membership.getApplications().add(application);
        getOnlinePlayers().filter(this::playerHasPermissions).forEach(x -> {
            NetworkUtils.sendToClient(new S2CSyncRenderMatPacket(player.getName().getString(), S2CSyncRenderMatPacket.Action.ADD, S2CSyncRenderMatPacket.Type.APPLICATION), x);
            NetworkUtils.sendToClient(new S2CTeamAppliedPacket(core.name(), player.getUUID()), x);
        });
    }

    public boolean isApplying(ServerPlayer player) {
        return membership.getApplications().contains(new Application(player.getUUID()));
    }

    public void tickApplication() {
        Iterator<Application> iterator = membership.getApplications().iterator();
        while (iterator.hasNext()){
            Application next = iterator.next();
            if (next.update()){
                iterator.remove();
                ServerPlayer player = teamData.serverLevel.getServer().getPlayerList().getPlayer(next.applicant);
                if (player == null) throw new NullPointerException("can't find player with UUID " + next.applicant + " when try to remove application.");
                getOnlinePlayers().filter(this::playerHasPermissions).forEach(x -> NetworkUtils.sendToClient(new S2CSyncRenderMatPacket(player.getName().getString(), S2CSyncRenderMatPacket.Action.REMOVE, S2CSyncRenderMatPacket.Type.APPLICATION), x));
            }
        }
    }
    public void markApplicationAsRemoval(UUID target){
        membership.getApplications().stream().filter(x -> x.applicant.equals(target)).findFirst().ifPresent(Application::markRemoval);
    }

    public boolean playerHasPermissions(ServerPlayer player) {
        return membership.playerHasPermissions(player);
    }

    public com.clefal.nirvana_lib.relocated.io.vavr.collection.List<ServerPlayer> getOnlinePlayers() {
        return membership.getOnlinePlayers();
    }

    public boolean isEmpty() {
        return membership.getMembers().isEmpty();
    }

    public boolean hasPlayer(ServerPlayer player) {
        return hasPlayer(player.getUUID());
    }

    public boolean hasPlayer(UUID player) {
        return membership.getMembers().contains(player);
    }

    public void addPlayer(ServerPlayer player) {
        addPlayer(player.getUUID());
    }

    public void removePlayer(ServerPlayer player) {
        removePlayer(player.getUUID());
    }

    public void onDisband() {
        var playersCopy = new ArrayList<>(membership.getMembers());
        playersCopy.forEach(this::removePlayer);
        attachments.getVanillaTeamAttachment().getVanillaTeam().getScoreboard().removePlayerTeam(attachments.getVanillaTeamAttachment().getVanillaTeam());
    }

    public void addAdvancement(Advancement advancement) {
        attachments.getAdvancementSyncAttachment().addAdvancement(advancement);
    }

    public void onPlayerOnline(ServerPlayer player, boolean sendPackets) {
        Map<UUID, ServerPlayer> onlineMembers = membership.getOnlineMembers();
        onlineMembers.put(player.getUUID(), player);
        ((IHasTeam) player).setTeam(this);
        String name = core.name();
        UUID leader = membership.getLeader();
        // Packets
        if (sendPackets) {
            NetworkUtils.sendToClient(new S2CTeamInitPacket(name, leader), player);
            if (onlineMembers.size() == 1) {
                var players = teamData.serverLevel.getServer().getPlayerList().getPlayers();
                NetworkUtils.sendToClients(new S2CTeamDataUpdatePacket(S2CTeamDataUpdatePacket.Type.ONLINE, name), players);
            }
            var players = getOnlinePlayers().toJavaList();
            NetworkUtils.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.ADD), players);

            for (var teammate : players) {
                NetworkUtils.sendToClient(new S2CTeamPlayerDataPacket(teammate, S2CTeamPlayerDataPacket.Type.ADD), player);
                NetworkUtils.sendToClient(new S2CPermissionUpdatePacket(playerHasPermissions(teammate), leader), player);
            }
        }
        // Advancement Sync
        attachments.conditionallyGetAdvancementSyncAttachment((ATServerConfig.config.shareAchievements.equals(ATServerConfig.Case.enable) && config.syncAdvancement) || ATServerConfig.config.shareAchievements.equals(ATServerConfig.Case.force))
                .map(x -> x.getAdvancements())
                .toStream()
                .flatMap(x -> x)
                .forEach(advancement -> {
                    AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
                    for (String criterion : progress.getRemainingCriteria()) {
                        player.getAdvancements().award(advancement, criterion);
                    }
                });
        //Application Sync
        if (playerHasPermissions(player)){
            for (Application application : membership.getApplications()) {
                ServerPlayer target = teamData.serverLevel.getServer().getPlayerList().getPlayer(application.applicant);
                if (target == null) throw new NullPointerException("can't find target with UUID " + application.applicant + " when try to add application.");
                NetworkUtils.sendToClient(new S2CSyncRenderMatPacket(target.getName().getString(), S2CSyncRenderMatPacket.Action.ADD, S2CSyncRenderMatPacket.Type.APPLICATION), player);
            }
        }

        AdvancedTeam.post(new ServerOnPlayerOnlineEvent(player));
    }


    public void onPlayerOffline(ServerPlayer player, boolean sendPackets) {
        membership.getOnlineMembers().remove(player.getUUID());
        // Packets
        if (sendPackets) {
            if (isEmpty()) {
                var players = teamData.serverLevel.getServer().getPlayerList().getPlayers();
                NetworkUtils.sendToClients(new S2CTeamDataUpdatePacket(S2CTeamDataUpdatePacket.Type.OFFLINE, core.name()), players);
            }
            var players = getOnlinePlayers();
            NetworkUtils.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.REMOVE), players.toJavaList());
        }
    }

    private void addPlayer(UUID player) {
        membership.getMembers().add(player);
        String playerName = getNameFromUUID(player);
        // Scoreboard
        attachments.conditionallyGetVanillaTeamAttachment((ATServerConfig.config.enableVanillaTeamCompat.equals(ATServerConfig.Case.enable) && config.enableVanillaTeamCompat) || ATServerConfig.config.enableVanillaTeamCompat.equals(ATServerConfig.Case.force))
                .map(x -> x.getVanillaTeam())
                .filter(x -> teamData.scoreboard.getPlayersTeam(playerName) == null || teamData.scoreboard.getPlayersTeam(playerName).isAlliedTo(x))
                .forEach(x -> teamData.scoreboard.addPlayerToTeam(playerName, x));

        var playerEntity = teamData.serverLevel.getServer().getPlayerList().getPlayer(player);
        if (playerEntity != null) {
            // Packets
            NetworkUtils.sendToClient(new S2CTeamUpdatePacket(core.name(), playerName, S2CTeamUpdatePacket.Action.JOINED, true), playerEntity);
            NetworkUtils.sendToClients(new S2CTeamUpdatePacket(core.name(), playerName, S2CTeamUpdatePacket.Action.JOINED, false), getOnlinePlayers().toJavaList());
            onPlayerOnline(playerEntity, true);
            // Advancement Sync
            Set<Advancement> advancements = ((AdvancementAccessor) playerEntity.getAdvancements()).getVisibleAdvancements();
            for (Advancement advancement : advancements) {
                if (playerEntity.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    addAdvancement(advancement);
                }
            }

            //clean invitation
            ((IHasTeam) playerEntity).clearInvitations();
            membership.getApplications().stream().filter(x -> x.applicant.equals(player)).findFirst().ifPresent(ExpirableObject::markRemoval);

        }

        teamData.setDirty();
    }

    private void removePlayer(UUID player) {
        membership.getMembers().remove(player);
        //find a new leader
        if (membership.getLeader().equals(player) && getOnlinePlayers().size() > 1) {
            Iterator<ServerPlayer> iterator = getOnlinePlayers().iterator();
            if (iterator.hasNext()) {
                this.promote(iterator.next());

            }
        } else if (getOnlinePlayers().size() == 1) {
            membership.setLeader(getOnlinePlayers().getOrElseThrow(() -> new NullPointerException("can't find the last player after removing, even there is still a player in team!")).getUUID());
        }
        String playerName = getNameFromUUID(player);
        // Scoreboard
        attachments.conditionallyGetVanillaTeamAttachment((ATServerConfig.config.enableVanillaTeamCompat.equals(ATServerConfig.Case.enable) && config.enableVanillaTeamCompat) || ATServerConfig.config.enableVanillaTeamCompat.equals(ATServerConfig.Case.force))
                .map(x -> x.getVanillaTeam())
                .filter(x -> teamData.scoreboard.getPlayersTeam(playerName) == null || teamData.scoreboard.getPlayersTeam(playerName).isAlliedTo(x))
                .forEach(x -> teamData.scoreboard.removePlayerFromTeam(playerName, x));

        // Packets
        var playerEntity = teamData.serverLevel.getServer().getPlayerList().getPlayer(player);
        if (playerEntity != null) {

            onPlayerOffline(playerEntity, true);
            NetworkUtils.sendToClient(new S2CTeamClearPacket(), playerEntity);
            NetworkUtils.sendToClient(new S2CTeamUpdatePacket(core.name(), playerName, S2CTeamUpdatePacket.Action.LEFT, true), playerEntity);
            NetworkUtils.sendToClients(new S2CTeamUpdatePacket(core.name(), playerName, S2CTeamUpdatePacket.Action.LEFT, false), getOnlinePlayers().toJavaList());
            ((IHasTeam) playerEntity).setTeam(null);
        }
        teamData.setDirty();
    }

    private String getNameFromUUID(UUID id) {
        return teamData.serverLevel.getServer().getProfileCache().get(id).map(GameProfile::getName).orElseThrow();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ATServerTeam that)) return false;
        return Objects.equal(core, that.core);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(core);
    }

    @Override
    public void postProcess(ServerLevel level, ATServerTeamData data, ATServerTeam team) {
        this.teamData = data;
        this.membership.postProcess(level, data, this);
        this.attachments.postProcess(level, data, this);
    }

    public static class Builder {

        private final String name;

        public Builder(String name) {
            this.name = name;
        }

        public ATServerTeam complete(ATServerTeamData teamData, UUID leader) {
            ATServerTeam team = new ATServerTeam(name, teamData, leader);
            return team;
        }
    }
}
