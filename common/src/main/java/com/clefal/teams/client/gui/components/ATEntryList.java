package com.clefal.teams.client.gui.components;

import com.clefal.teams.AdvancedTeam;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class ATEntryList extends AbstractSelectionList<ATEntryList.ATEntry> {

    static final int WIDTH = 244;
    static final int HEIGHT = 24;
    private int rowWidth = 200;
    public static final int buttonXInterval = 24;
    public static final int buttonYInterval = 8;
    private static final ResourceLocation TEXTURE = AdvancedTeam.id("textures/gui/screen_background.png");

    public ATEntryList(int width, int height, int y0, int y1) {
        super(Minecraft.getInstance(), width, height, y0, y1, HEIGHT);
        //setLeftPos(-10);
        this.setRenderBackground(false);
        this.setRenderHeader(false, 0);
        this.setRenderTopAndBottom(false);
        this.setRenderSelection(false);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.width / 2 + 115;
    }


    //not WIDTH!!
    @Override
    public int getRowWidth() {
        return rowWidth;
    }



    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }


    public abstract class ATEntry extends AbstractSelectionList.Entry<ATEntry>{

        @Override
        public void renderBack(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            RenderSystem.enableDepthTest();
            guiGraphics.blitNineSliced(TEXTURE, left, top, rowWidth, HEIGHT, 5, 5, 5, 5, WIDTH, HEIGHT, 0, 166);
        }
    }
}
