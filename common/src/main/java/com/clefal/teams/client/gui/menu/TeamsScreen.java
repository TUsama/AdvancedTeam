package com.clefal.teams.client.gui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.clefal.teams.client.core.ClientTeam;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class TeamsScreen extends Screen {

    public final Screen parent;
    protected int x;
    protected int y;
    protected boolean inTeam;

    public TeamsScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
        inTeam = ClientTeam.INSTANCE.isInTeam();
    }

    @Override
    protected void init() {
        x = (width - getWidth()) / 2;
        y = (height - getHeight()) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        renderBackground(graphics);
        graphics.pose().pushPose();
        graphics.blit(getBackgroundTexture(), x, y, 0, 0, getWidth(), getHeight());
        graphics.pose().popPose();
        super.render(graphics, mouseX, mouseY, delta);
    }

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected abstract ResourceLocation getBackgroundTexture();
    
}
