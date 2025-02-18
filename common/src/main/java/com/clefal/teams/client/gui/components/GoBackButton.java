package com.clefal.teams.client.gui.components;

import com.clefal.teams.AdvancedTeam;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GoBackButton extends Button {
    private final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/go_back.png");
    public GoBackButton(int x, int y, int width, int height, Component message, OnPress onPress) {
        super(x, y, width, height, message, onPress, Button.DEFAULT_NARRATION);
        setTooltip(Tooltip.create(message));
    }


    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isHoveredOrFocused()){
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), getWidth(), getHeight(), 0, 0, 16, 16, 16, 32);
        } else {
            guiGraphics.blit(TEXTURE, this.getX(), this.getY(), getWidth(), getHeight(), 0, 16, 16, 16, 16, 32);
        }

    }
}
