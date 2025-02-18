package com.clefal.teams.client.gui.screens;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public abstract class TeamMenuTab extends AbstractButton {


    public TeamMenuTab(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }


    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
