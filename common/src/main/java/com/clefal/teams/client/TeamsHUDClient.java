package com.clefal.teams.client;

import com.clefal.teams.client.ui.toast.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.properties.Property;
import com.clefal.teams.ScreenDuck;
import com.clefal.teams.TeamsHUD;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.ClientTeamData;
import com.clefal.teams.client.ui.hud.CompassOverlay;
import com.clefal.teams.client.ui.hud.StatusOverlay;
import com.clefal.teams.client.ui.menu.TeamsLonelyScreen;
import com.clefal.teams.client.ui.menu.TeamsMainScreen;
import com.clefal.teams.mixin.InventoryScreenAccessor;
import com.clefal.teams.network.client.S2CTeamPlayerDataPacket;
import com.clefal.teams.network.client.S2CTeamUpdatePacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;

import java.util.UUID;

public class TeamsHUDClient {

    public static final StatusOverlay status = new StatusOverlay();
    public static final CompassOverlay compass = new CompassOverlay();

    public static void registerKeybinding(KeyMapping keyMapping) {
        Minecraft.getInstance().options.keyMappings = ArrayUtils.add(Minecraft.getInstance().options.keyMappings, keyMapping);
    }

    public static final ResourceLocation TEAMS_BUTTON_TEXTURE = TeamsHUD.id("textures/gui/buttonsmall.png");

    public static void registerKeybinds() {
        // Register keybinds
        for (TeamsKeys.TeamsKey key : TeamsKeys.KEYS) {
            key.register();
        }
    }

    public static void clientDisconnect() {
        ClientTeam.INSTANCE.reset();
        ClientTeamData.INSTANCE.clear();
    }

    public static void afterScreenInit(Minecraft minecraft, Screen screen, int scaledWidth, int scaledHeight){
        if (screen instanceof InventoryScreen inventoryScreen && minecraft.gameMode != null && !minecraft.gameMode.hasInfiniteItems()) {
            InventoryScreenAccessor screenAccessor = ((InventoryScreenAccessor) screen);
            ((ScreenDuck)inventoryScreen).$addButton(new ImageButton(screenAccessor.getX() + screenAccessor.getBackgroundWidth() - 19, screenAccessor.getY() + 4, 15, 14, 0, 0, 13, TEAMS_BUTTON_TEXTURE, (button) -> {
                if (ClientTeam.INSTANCE.isInTeam()) {
                    minecraft.setScreen(new TeamsMainScreen(minecraft.screen));

                } else {
                    minecraft.setScreen(new TeamsLonelyScreen(minecraft.screen));
                }
            }){
                @Override
                protected boolean clicked(double pMouseX, double pMouseY) {
                    return this.active && this.visible && pMouseX >= (double)this.getX() && pMouseY >= (double)this.getY() && pMouseX < (double)(this.getX() + this.width) && pMouseY < (double)(this.getY() + this.height);
                }
            });
        }
    }

    public static void endClientTick() {
        for (var key : TeamsKeys.KEYS) {
            if (key.keyBinding.consumeClick()) {
                key.onPress.execute(Minecraft.getInstance());
            }
        }
    }


}
