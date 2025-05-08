package com.clefal.teams;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.HashSet;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.BusBuilder;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.Event;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.IEventBus;
import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.client.core.property.renderer.RendererManager;
import com.clefal.teams.config.ConfigManager;
import com.clefal.teams.event.client.ClientEvent;
import com.clefal.teams.event.server.ServerEvent;
import com.clefal.teams.event.server.ServerFreezePropertyUpdateEvent;
import com.clefal.teams.event.server.ServerPlayerTickJobEvent;
import com.clefal.teams.network.Packets;
import com.clefal.teams.network.client.S2CInvitationPacket;
import com.clefal.teams.network.client.S2CTeamDataUpdatePacket;
import com.clefal.teams.network.client.S2CTeamPlayerDataPacket;
import com.clefal.teams.platform.Services;
import com.clefal.teams.server.*;
import com.clefal.teams.modules.internal.HandlerManager;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ListIterator;

public class AdvancedTeam {
    public static final boolean IN_DEV = Boolean.getBoolean("at.dev.tool");
    public static final String MODID = "teams";
    public static final String MOD_NAME = "Advanced Team";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final IEventBus clientBus = BusBuilder.builder().setExceptionHandler((iEventBus, event, eventListeners, i, throwable) -> {
        try {
            throw throwable;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }).build();
    public static final IEventBus serverBus = BusBuilder.builder().setExceptionHandler((iEventBus, event, eventListeners, i, throwable) -> {
        try {
            throw throwable;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }).build();

    public static <T extends Event> T post(T t) {
        if (t instanceof ServerEvent) {
            AdvancedTeam.serverBus.post(t);
        } else if (t instanceof ClientEvent) {
            AdvancedTeam.clientBus.post(t);
        }
        return t;
    }

    public static void registerAtClient(Object o) {
        AdvancedTeam.clientBus.register(o);
    }

    public static void registerAtServer(Object o) {
        AdvancedTeam.serverBus.register(o);
    }

    public static void packetInit() {
        Packets.registerClientPackets();
        Packets.registerServerPackets();
    }

    public static void clientInit() {
        ConfigManager.init();
        for (var han : HandlerManager.INSTANCE.getClientHandlers()) {
            clientBus.register(han);
        }
        RendererManager.init();
    }

    public static void serverInit() {
        ConfigManager.init();
        for (var han : HandlerManager.INSTANCE.getServerHandlers()) {
            serverBus.register(han);
        }
        AdvancedTeam.serverBus.<ServerPlayerTickJobEvent>addListener(x -> onServerPlayerTick(x.player));
    }


    public static void onAdvancement(ServerPlayer player, Advancement advancement) {
        ATServerTeamData teamData = ATServerTeamData.getOrMakeDefault(player.server);
        ATServerTeam team = teamData.getTeam(player);
        if (team != null) {
            team.addAdvancement(advancement);
        }
    }

    public static void onServerPlayerTick(ServerPlayer player){
        IHasTeam hasTeam = (IHasTeam) player;
        IPropertySender propertySender = (IPropertySender) player;
        ListIterator<Invitation> invitationListIterator = hasTeam.getInvitations().listIterator();
        //tick invitation
        while (invitationListIterator.hasNext()) {
            Invitation next = invitationListIterator.next();
            if (next.update()) {
                invitationListIterator.remove();
                NetworkUtils.sendToClient(new S2CInvitationPacket(next.teamName, S2CInvitationPacket.Type.REMOVE), player);
            }
        }
        //tick property update.
        propertySender.handleUpdate();
        //AdvancedTeam.post(new ServerPlayerTickJobEvent(player));
    }

    public static void whenServerTick(MinecraftServer server){
        for (ATServerTeam team : ATServerTeamData.getOrMakeDefault(server).getTeams()) {
            if (team.getOnlinePlayers().find(team::playerHasPermissions).isDefined()){
                team.tickApplication();
            }
        }
    }

    public static void whenPlayerConnect(ServerPlayer player) {
        ATServerTeamData teamData = ATServerTeamData.getOrMakeDefault(player.server);
        ATServerTeam team = teamData.getTeam(player);
        if (team != null) {
            team.onPlayerOnline(player, true);
        }
        // Send packets
        var teams = teamData.getTeams().map(ATServerTeam::getName).toJavaArray(String[]::new);
        var onlineTeams = teamData.getTeams().filter(t -> !t.getOnlinePlayers().isEmpty()).map(ATServerTeam::getName).toJavaArray(String[]::new);
        NetworkUtils.sendToClient(new S2CTeamDataUpdatePacket(S2CTeamDataUpdatePacket.Type.ADD, teams), player);
        NetworkUtils.sendToClient(new S2CTeamDataUpdatePacket(S2CTeamDataUpdatePacket.Type.ONLINE, onlineTeams), player);
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