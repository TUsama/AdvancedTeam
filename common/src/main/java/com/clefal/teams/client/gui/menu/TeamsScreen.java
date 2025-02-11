package com.clefal.teams.client.gui.menu;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.config.ATClientConfig;
import com.clefal.teams.server.ModComponents;
import com.mojang.blaze3d.systems.RenderSystem;
import com.clefal.teams.client.core.ClientTeam;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class TeamsScreen extends Screen {
    public static final int WIDTH = 256;
    public static final int HEIGHT = 166;
    protected static final ResourceLocation TEXTURE = new ResourceLocation(AdvancedTeam.MODID, "textures/gui/screen_background.png");
    public final Screen parent;
    protected int x;
    @Getter
    protected int y;
    protected boolean inTeam;
    protected Button GO_BACK;

    public TeamsScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
        inTeam = ClientTeam.INSTANCE.isInTeam();
    }

    @Override
    protected void init() {
        x = (width - getWidth()) / 2;
        y = (height - getHeight()) / 2;
        GO_BACK = Button.builder(ModComponents.GO_BACK_TEXT, button -> minecraft.setScreen(parent)).bounds(this.width / 2  + 45, y + HEIGHT - 30, 80, 20).build();
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
