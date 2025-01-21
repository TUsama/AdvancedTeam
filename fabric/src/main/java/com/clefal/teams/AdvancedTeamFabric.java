package com.clefal.teams;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;

public class AdvancedTeamFabric implements ModInitializer {


    @Override
    public void onInitialize() {
        AdvancedTeam.LOGGER.info("Teams fabric mod init!");

        ServerLifecycleEvents.SERVER_STARTED.register(AdvancedTeam::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPED.register(AdvancedTeam::onServerStopped);


        // Event hooks
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayer player = handler.player;
            AdvancedTeam.whenPlayerConnect(player);
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayer player = handler.player;
            AdvancedTeam.whenPlayerOffline(player);
        });
        ServerPlayerEvents.COPY_FROM.register(AdvancedTeam::whenplayerClone);
        AdvancedTeam.init();
    }
}
