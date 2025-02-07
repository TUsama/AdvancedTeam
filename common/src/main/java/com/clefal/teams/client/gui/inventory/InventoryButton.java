package com.clefal.teams.client.gui.inventory;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.menu.noteam.NoTeamScreen;
import com.clefal.teams.client.gui.menu.TeamsMainScreen;
import com.clefal.teams.config.ATClientConfig;
import com.clefal.teams.mixin.InventoryScreenAccessor;
import com.clefal.teams.mixinhelper.ScreenDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;

public class InventoryButton {
    public static final ResourceLocation TEAMS_BUTTON_TEXTURE = AdvancedTeam.id("textures/gui/buttonsmall.png");

    public static void afterScreenInit(Minecraft minecraft, Screen screen, int scaledWidth, int scaledHeight){
        if (!ATClientConfig.config.button.enableInventoryButton) return;
        if (screen instanceof InventoryScreen inventoryScreen && minecraft.gameMode != null && !minecraft.gameMode.hasInfiniteItems()) {
            InventoryScreenAccessor screenAccessor = ((InventoryScreenAccessor) screen);
            ((ScreenDuck)inventoryScreen).$addButton(new ImageButton(screenAccessor.getX() + screenAccessor.getBackgroundWidth() - 19 + ATClientConfig.config.button.inventoryButtonXOffset, screenAccessor.getY() + 4 + ATClientConfig.config.button.inventoryButtonYOffset, 15, 14, 0, 0, 13, TEAMS_BUTTON_TEXTURE, (button) -> {
                if (ClientTeam.INSTANCE.isInTeam()) {
                    minecraft.setScreen(new TeamsMainScreen(minecraft.screen));

                } else {
                    minecraft.setScreen(new NoTeamScreen(minecraft.screen));
                }
            }){
                @Override
                protected boolean clicked(double pMouseX, double pMouseY) {
                    return this.active && this.visible && pMouseX >= (double)this.getX() && pMouseY >= (double)this.getY() && pMouseX < (double)(this.getX() + this.width) && pMouseY < (double)(this.getY() + this.height);
                }
            });
        }
    }
}
