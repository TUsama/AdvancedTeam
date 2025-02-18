package com.clefal.teams.client.gui.screens.invite;

import com.clefal.teams.client.gui.screens.TeamsScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class CheckInvitationScreen extends TeamsScreen {

    private InvitationEntryList entryList;

    public CheckInvitationScreen(Screen parent) {
        super(parent, Component.literal(""));

    }


    @Override
    protected void init() {
        super.init();
        this.entryList = new InvitationEntryList(this);
        addRenderableWidget(entryList);
        GO_BACK.setX(this.width / 2);
        addRenderableWidget(GO_BACK);
    }

    public void update(){
        this.entryList = new InvitationEntryList(this);
        addRenderableWidget(entryList);
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
