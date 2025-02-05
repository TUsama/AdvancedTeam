package com.clefal.teams.client.gui.components;

import com.clefal.teams.AdvancedTeam;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.resources.ResourceLocation;

public abstract class ATEntryList extends AbstractSelectionList<ATEntryList.ATEntry> {

    static final int WIDTH = 200;
    static final int HEIGHT = 24;
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



    @Override
    public int getRowWidth() {
        return WIDTH;
    }



    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }


    public abstract class ATEntry extends AbstractSelectionList.Entry<ATEntry>{

        @Override
        public void renderBack(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            guiGraphics.blit(TEXTURE, left, top, 0, 166, WIDTH, HEIGHT);
        }
    }
}
