package com.clefal.teams;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.BusBuilder;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.IEventBus;
import com.clefal.teams.core.ModTeam;
import com.clefal.teams.core.TeamData;
import com.clefal.teams.network.CommonPacketHandler;
import com.clefal.teams.network.client.S2CTeamDataPacket;
import com.clefal.teams.network.client.S2CTeamPlayerDataPacket;
import com.clefal.teams.platform.Services;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TeamsHUD {

    public static final String MODID = "teams";
    public static final String MOD_NAME = "TeamsHUD";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final IEventBus bus = BusBuilder.builder().setExceptionHandler((iEventBus, event, eventListeners, i, throwable) -> {
        try {
            throw throwable;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }).build();


    public static void init() {
        CommonPacketHandler.registerPackets();
    }

    public static void onAdvancement(ServerPlayer player, Advancement advancement) {
        TeamData teamData = TeamData.getOrMakeDefault(player.server);
        ModTeam team = teamData.getTeam(player);
        if (team != null) {
            team.addAdvancement(advancement);
        }
    }

    public static void playerConnect(ServerPlayer player) {
        TeamData teamData = TeamData.getOrMakeDefault(player.server);
        ModTeam team = teamData.getTeam(player);
        if (team != null) {
            team.playerOnline(player, true);
        }
        // Send packets
        var teams = teamData.getTeams().map(t -> t.name).toArray(String[]::new);
        var onlineTeams = teamData.getTeams().filter(t -> t.getOnlinePlayers().stream().findAny().isPresent()).map(t -> t.name).toArray(String[]::new);
        Services.PLATFORM.sendToClient(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ADD, teams), player);
        Services.PLATFORM.sendToClient(new S2CTeamDataPacket(S2CTeamDataPacket.Type.ONLINE, onlineTeams), player);
    }

    public static void playerDisconnect(ServerPlayer player) {
        TeamData teamData = TeamData.getOrMakeDefault(player.server);
        ModTeam team = teamData.getTeam(player);
        if (team != null) {
            team.playerOffline(player, true);
        }
    }

    public static void playerClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
        TeamData teamData = TeamData.getOrMakeDefault(oldPlayer.server);
        ModTeam team = teamData.getTeam(oldPlayer);
        if (team != null) {
            team.playerOffline(oldPlayer, false);
            team.playerOnline(newPlayer, false);
        }
    }

    public static void onPlayerHealthUpdate(ServerPlayer player, float health, int hunger) {
        ModTeam team = TeamData.getOrMakeDefault(player.server).getTeam(player);
        if (team != null) {
            List<ServerPlayer> players = team.getOnlinePlayers().stream().filter(other -> !other.equals(player)).collect(Collectors.toList());
            Services.PLATFORM.sendToClients(new S2CTeamPlayerDataPacket(player, S2CTeamPlayerDataPacket.Type.UPDATE), players);
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MODID, path);
    }

    public static void onServerStopped(MinecraftServer server) {

    }

    public static void onServerStarted(MinecraftServer server) {

    }
}