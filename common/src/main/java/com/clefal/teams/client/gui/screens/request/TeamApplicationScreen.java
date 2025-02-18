package com.clefal.teams.client.gui.screens.request;

import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.network.server.C2SAskNoTeamPlayerWithSkinPacket;
import com.clefal.teams.network.server.C2SAskRequestPlayerWithSkinPacket;
import com.clefal.teams.platform.Services;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TeamApplicationScreen extends TeamsScreen {
    public ApplicationsList entryList;

    public TeamApplicationScreen(Screen parent) {
        super(parent, Component.literal(""));
    }

    @Override
    protected void init() {
        super.init();
        this.entryList = new ApplicationsList(this);
        Services.PLATFORM.sendToServer(new C2SAskRequestPlayerWithSkinPacket());
        addRenderableOnly(entryList);
    }

    @Override
    protected int getWidth() {
        return WIDTH;
    }

    @Override
    protected int getHeight() {
        return HEIGHT;
    }

    @Override
    protected ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }
}
