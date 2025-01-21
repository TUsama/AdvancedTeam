package com.clefal.teams.client;

import com.clefal.teams.AdvancedTeamsHUDClient;
import com.clefal.teams.client.gui.hud.CompassOverlay;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import com.clefal.teams.client.gui.inventory.InventoryButton;
import com.clefal.teams.client.keybind.TeamsKeys;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.ArrayUtils;

public class AdvancedTeamClientForge {


    public static void init(IEventBus bus) {

        bus.addListener(AdvancedTeamClientForge::setup);
        bus.addListener(AdvancedTeamClientForge::registerOverlays);
        MinecraftForge.EVENT_BUS.addListener(AdvancedTeamClientForge::clientTick);
        MinecraftForge.EVENT_BUS.addListener(AdvancedTeamClientForge::clientDisconnect);
        MinecraftForge.EVENT_BUS.addListener(AdvancedTeamClientForge::addButton);
    }

    static void clientDisconnect(ClientPlayerNetworkEvent.LoggingOut event) {
        AdvancedTeamsHUDClient.resetClientTeamStatus();
    }
    static void clientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            TeamsKeys.consumerKeys();
        }
    }

    static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("compass",(gui, graphics, partialTick, width, height) -> StatusOverlay.INSTANCE.render(graphics));
        event.registerAboveAll("status",(gui, graphics, partialTick, width, height) -> CompassOverlay.INSTANCE.render(graphics));
    }

    static void addButton(ScreenEvent.Init.Post event) {
        InventoryButton.afterScreenInit(Minecraft.getInstance(),event.getScreen(),Minecraft.getInstance().getWindow().getGuiScaledWidth(),Minecraft.getInstance().getWindow().getGuiScaledHeight());
    }

    static void setup(FMLClientSetupEvent event) {
        TeamsKeys.registerAllKey(keyMapping -> Minecraft.getInstance().options.keyMappings = ArrayUtils.addAll(Minecraft.getInstance().options.keyMappings, keyMapping));
    }
}
