package com.clefal.teams;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.BusBuilder;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.IEventBus;
import com.clefal.teams.config.ConfigManager;
import com.clefal.teams.server.ATServerTeam;
import com.clefal.teams.server.ATServerTeamData;
import com.clefal.teams.event.client.ClientEvent;
import com.clefal.teams.event.server.ServerEvent;
import com.clefal.teams.network.CommonPacketHandler;
import com.clefal.teams.network.client.S2CTeamDataUpdatePacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.propertyhandler.HandlerManager;
import com.clefal.teams.server.propertyhandler.IPropertyHandler;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdvancedTeam {

    public static final String MODID = "teams";
    public static final String MOD_NAME = "Advanced Team";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final IEventBus eventBus = BusBuilder.builder().setExceptionHandler((iEventBus, event, eventListeners, i, throwable) -> {
        try {
            throw throwable;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }).build();



    public static void init() {
        CommonPacketHandler.registerPackets();
        ConfigManager.init();
        for (IPropertyHandler handler : HandlerManager.INSTANCE.getHandlers()) {
            eventBus.register(handler);
        }
    }


    public static void onAdvancement(ServerPlayer player, Advancement advancement) {
        ATServerTeamData teamData = ATServerTeamData.getOrMakeDefault(player.server);
        ATServerTeam team = teamData.getTeam(player);
        if (team != null) {
            team.addAdvancement(advancement);
        }
    }

    public static void whenPlayerConnect(ServerPlayer player) {
        ATServerTeamData teamData = ATServerTeamData.getOrMakeDefault(player.server);
        ATServerTeam team = teamData.getTeam(player);
        if (team != null) {
            team.onPlayerOnline(player, true);
        }
        // Send packets
        var teams = teamData.getTeams().map(t -> t.name).toArray(String[]::new);
        var onlineTeams = teamData.getTeams().filter(t -> t.getOnlinePlayers().stream().findAny().isPresent()).map(t -> t.name).toArray(String[]::new);
        Services.PLATFORM.sendToClient(new S2CTeamDataUpdatePacket(S2CTeamDataUpdatePacket.Type.ADD, teams), player);
        Services.PLATFORM.sendToClient(new S2CTeamDataUpdatePacket(S2CTeamDataUpdatePacket.Type.ONLINE, onlineTeams), player);
    }

    public static void whenPlayerOffline(ServerPlayer player) {
        ATServerTeamData teamData = ATServerTeamData.getOrMakeDefault(player.server);
        ATServerTeam team = teamData.getTeam(player);
        if (team != null) {
            team.onPlayerOffline(player, true);
        }
    }

    public static void whenplayerClone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean alive) {
        ATServerTeamData teamData = ATServerTeamData.getOrMakeDefault(oldPlayer.server);
        ATServerTeam team = teamData.getTeam(oldPlayer);
        if (team != null) {
            team.onPlayerOffline(oldPlayer, false);
            team.onPlayerOnline(newPlayer, false);
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