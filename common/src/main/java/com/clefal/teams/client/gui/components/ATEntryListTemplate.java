package com.clefal.teams.client.gui.components;

import com.clefal.teams.client.gui.screens.TeamsScreen;

public class ATEntryListTemplate extends ATEntryList{
    public ATEntryListTemplate(TeamsScreen screen) {
        super(screen.width, screen.height, screen.getY() + 4, screen.getY() + TeamsScreen.HEIGHT - 32);
    }
}
