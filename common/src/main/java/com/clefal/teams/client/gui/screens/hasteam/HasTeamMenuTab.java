package com.clefal.teams.client.gui.screens.hasteam;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.gui.screens.TeamMenuTab;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class HasTeamMenuTab extends TeamMenuTab {

    private final static ResourceLocation background = AdvancedTeam.id("textures/gui/tab.png");
    protected HasTeamScreen screen;
    protected ResourceLocation texture;


    public HasTeamMenuTab(Component message, HasTeamScreen screen, ResourceLocation texture) {
        super(0, 0, 14 * 2, 9 * 2, message);
        this.screen = screen;
        this.texture = texture;
        setTooltip(Tooltip.create(message));
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        pose.translate(0, 0, -10.1);
        if (isHoveredOrFocused() || isInItsMenu()){
            pose.translate(-3, -0.3f, 0);
        }
        guiGraphics.blit(background, this.getX(), this.getY(), this.width, this.height, 0, 0, 14, 9, 14, 9);
        renderIcon(guiGraphics, mouseX, mouseY, partialTick);
        pose.popPose();
    }

    public abstract boolean isInItsMenu();

    public abstract void renderIcon(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

}
