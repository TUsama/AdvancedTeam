package com.clefal.teams.client.gui.screens;

import net.minecraft.client.gui.GuiGraphics;

public interface ITeamMenu {

    void init();
    void render(GuiGraphics graphics, int mouseX, int mouseY, float delta);
}
