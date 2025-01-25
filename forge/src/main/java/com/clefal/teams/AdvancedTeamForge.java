package com.clefal.teams;

import com.clefal.teams.client.AdvancedTeamClientForge;
import com.clefal.teams.compat.CompatManager;
import com.clefal.teams.compat.MineAndSlashCompatModule;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(AdvancedTeam.MODID)
public class AdvancedTeamForge {
    
    public AdvancedTeamForge() {
        AdvancedTeam.LOGGER.info("Teams forge mod init!");
        IEventBus bus  = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.addListener(this::login);
        MinecraftForge.EVENT_BUS.addListener(this::logout);
        MinecraftForge.EVENT_BUS.addListener(this::playerClone);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStopped);
        MinecraftForge.EVENT_BUS.addListener(this::onAdvancement);

        AdvancedTeam.init();

        if (FMLEnvironment.dist.isClient()) {
            AdvancedTeamClientForge.init(bus);
        }

        CompatManager.compats.add(MineAndSlashCompatModule.INSTANCE);
        CompatManager.tryEnableAll();

    }



    private void onAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        AdvancedTeam.onAdvancement((ServerPlayer) event.getEntity(),event.getAdvancement());
    }

    private void onServerStarted(ServerStartedEvent event) {
        AdvancedTeam.onServerStarted(event.getServer());
    }

    private void onServerStopped(ServerStoppedEvent event) {
        AdvancedTeam.onServerStopped(event.getServer());
    }

    private void login(PlayerEvent.PlayerLoggedInEvent event) {
        AdvancedTeam.whenPlayerConnect((ServerPlayer) event.getEntity());
    }

    private void logout(PlayerEvent.PlayerLoggedOutEvent event) {
        AdvancedTeam.whenPlayerOffline((ServerPlayer) event.getEntity());
    }

    private void playerClone(PlayerEvent.Clone event) {
        AdvancedTeam.whenplayerClone((ServerPlayer) event.getOriginal(), (ServerPlayer) event.getEntity(),!event.isWasDeath());
    }


    private void commonSetup(FMLCommonSetupEvent event) {
    }

}