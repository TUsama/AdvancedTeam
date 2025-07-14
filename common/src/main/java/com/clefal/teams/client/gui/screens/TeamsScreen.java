package com.clefal.teams.client.gui.screens;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.gui.components.GoBackButton;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.client.core.ClientTeam;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public abstract class TeamsScreen extends Screen {
    public static final int WIDTH = 256;
    public static final int HEIGHT = 166;
    protected static final ResourceLocation TEXTURE = new ResourceLocation(AdvancedTeam.MODID, "textures/gui/screen_background.png");
    @Nullable
    public final Screen parent;
    @Getter
    protected int x;
    @Getter
    protected int y;
    protected boolean inTeam;
    protected GoBackButton GO_BACK;

    public TeamsScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
        inTeam = ClientTeam.INSTANCE.isInTeam();
    }

    @Override
    protected void init() {
        x = (width - getWidth()) / 2;
        y = (height - getHeight()) / 2;
        GO_BACK = new GoBackButton(x, y - 25, 25, 25, ModComponents.GO_BACK_TEXT,  button -> minecraft.setScreen(parent));
        addRenderableWidget(GO_BACK);
    }


    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        RenderSystem.enableDepthTest();
        super.render(graphics, mouseX, mouseY, delta);
        graphics.pose().pushPose();

        graphics.blit(getBackgroundTexture(), x, y, -10, 0, 0, getWidth(), getHeight(), 256, 256);
        graphics.pose().translate(0, 0, -11);
        renderBackground(graphics);
        graphics.pose().popPose();

    }

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected abstract ResourceLocation getBackgroundTexture();

    public void refresh(){

    }


}
