package com.clefal.teams.client.gui.menu.invite;

import com.clefal.teams.client.gui.menu.TeamsScreen;
import com.clefal.teams.network.server.C2SInviteScreenAskPacket;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.platform.Services;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TeamsInvitePlayerScreen extends TeamsScreen {

    public AvailablePlayersList entryList;

    public TeamsInvitePlayerScreen(Screen parent) {
        super(parent, ModComponents.INVITE_TITLE_TEXT);
    }

    @Override
    protected void init() {
        super.init();
        this.entryList = new AvailablePlayersList(this.width, this.height, this.y - 4, y + HEIGHT - 32, List.of());
        addRenderableWidget(this.entryList);
        Services.PLATFORM.sendToServer(new C2SInviteScreenAskPacket());
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
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
