package com.clefal.teams.client.gui.screens.invite;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.client.PlaceUtils;
import com.clefal.teams.client.gui.screens.TeamsScreen;
import com.clefal.teams.network.server.C2SAskNoTeamPlayerWithSkinPacket;
import com.clefal.teams.network.server.C2STeamInvitePacket;
import com.clefal.teams.server.ModComponents;

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
        NetworkUtils.sendToServer(new C2SAskNoTeamPlayerWithSkinPacket());
        addRenderableWidget(PlaceUtils.placeOneButton(this, Button.builder(Component.translatable("teams.menu.batch_invite"), button -> {
                    NetworkUtils.sendToServer(new C2STeamInvitePacket(this.entryList.getAllSelection()));
                    Minecraft.getInstance().setScreen(parent);
                })
                .bounds(this.width / 2 - 106, y + HEIGHT - 30, 100, 20)
                .build()));
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
