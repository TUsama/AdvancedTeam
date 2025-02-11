package com.clefal.teams.client.gui.menu.invite;

import com.clefal.teams.client.gui.menu.TeamsScreen;
import com.clefal.teams.network.server.C2SInviteScreenAskPacket;
import com.clefal.teams.network.server.C2STeamInvitePacket;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
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
        this.entryList = new AvailablePlayersList(this, List.of());
        addRenderableWidget(this.entryList);
        Services.PLATFORM.sendToServer(new C2SInviteScreenAskPacket());
        addRenderableWidget(Button.builder(Component.translatable("teams.menu.batch_invite"), button -> {
            Services.PLATFORM.sendToServer(new C2STeamInvitePacket(this.entryList.getAllSelection()));
            Minecraft.getInstance().setScreen(parent);
        })
                .bounds(this.width / 2 - 106, y + HEIGHT - 30, 100, 20)
                .build());
        GO_BACK.setPosition(this.width / 2 + 6, y + HEIGHT - 30);
        addRenderableWidget(GO_BACK);
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
