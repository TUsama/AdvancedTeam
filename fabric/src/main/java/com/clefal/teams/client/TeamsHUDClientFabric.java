package com.clefal.teams.client;

import com.clefal.teams.AdvancedTeamsHUDClient;
import com.clefal.teams.client.gui.hud.CompassOverlay;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import com.clefal.teams.client.gui.inventory.InventoryButton;
import com.clefal.teams.client.keybind.TeamsKeys;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.KeyMapping;

public class TeamsHUDClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Register HUDs
        HudRenderCallback.EVENT.register((graphics, tickDelta) -> {
            StatusOverlay.INSTANCE.render(graphics);
            CompassOverlay.INSTANCE.render(graphics);
        });


        // Register events
        ClientLoginConnectionEvents.DISCONNECT.register((handler, client) -> AdvancedTeamsHUDClient.resetClientTeamStatus());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> AdvancedTeamsHUDClient.resetClientTeamStatus());

        //add button on inventory
        ScreenEvents.AFTER_INIT.register(InventoryButton::afterScreenInit);

        // Handle keybinds
        TeamsKeys.registerAllKey(keyMappings -> {
            for (KeyMapping keyMapping : keyMappings) {
                KeyBindingHelper.registerKeyBinding(keyMapping);
            }
        });
        ClientTickEvents.END_CLIENT_TICK.register(client -> TeamsKeys.consumerKeys());

    }
}
