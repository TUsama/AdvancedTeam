package com.clefal.teams.client.gui.screens.hasteam;

import com.clefal.teams.client.gui.screens.ITeamMenu;

public abstract class HasTeamMenu implements ITeamMenu {
    HasTeamScreen screen;

    public HasTeamMenu(HasTeamScreen screen) {
        this.screen = screen;
    }

    public abstract void registerAll();

    public abstract void releaseAll();

    public abstract HasTeamMenuTab getTab();
}
