package com.clefal.teams.client.gui.screens.noteam;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.gui.screens.TeamsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class InvitationScreen extends TeamsScreen {
    private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");
    public InvitationScreen(Screen parent) {
        super(parent, Component.literal(""));
    }

    @Override
    protected int getWidth() {
        return parent.width;
    }

    @Override
    protected int getHeight() {
        return parent.height;
    }

    @Override
    protected ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
