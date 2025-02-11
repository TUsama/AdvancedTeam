package com.clefal.teams.client.gui.components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;

public class ComponentButton extends Button {
    final int backgroundColor = FastColor.ARGB32.color(0, 255, 255, 255);
    final Component text;
    protected ComponentButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration, Component text) {
        super(x, y, width, height, message, onPress, createNarration);
        this.text = text;
    }

    public static Builder builder(Component message, OnPress onPress, Component component){
        return new Builder(message, onPress, component);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft.getInstance().font.drawInBatch(this.text, this.getX(), this.getY(), ChatFormatting.WHITE.getColor(), false, guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, backgroundColor, 15728880);

    }

    public static class Builder {
        private final Component message;
        private final OnPress onPress;
        private final Component component;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private CreateNarration createNarration;

        public Builder(Component message, OnPress onPress, Component component) {
            this.createNarration = Button.DEFAULT_NARRATION;
            this.message = message;
            this.onPress = onPress;
            this.component = component;
        }

        public ComponentButton.Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public ComponentButton.Builder width(int width) {
            this.width = width;
            return this;
        }

        public ComponentButton.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public ComponentButton.Builder bounds(int x, int y, int width, int height) {
            return this.pos(x, y).size(width, height);
        }

        public ComponentButton.Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public ComponentButton.Builder createNarration(CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public ComponentButton build() {
            ComponentButton button = new ComponentButton(this.x, this.y, this.width, this.height, this.message, this.onPress, this.createNarration, component);
            button.setTooltip(this.tooltip);
            return button;
        }
    }
}
