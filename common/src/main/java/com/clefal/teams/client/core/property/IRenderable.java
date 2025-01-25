package com.clefal.teams.client.core.property;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;

import java.util.concurrent.ThreadLocalRandom;

public interface IRenderable {

    float oneEntryHeight = StatusOverlay.getRelativeHeight(0.04f);
    float iconAndTextInterval = StatusOverlay.getRelativeWidth(0.03f);
    float barWidth = StatusOverlay.getRelativeWidth(0.2f);
    float barHeight = StatusOverlay.getRelativeHeight(0.04f);
    ThreadLocalRandom random = ThreadLocalRandom.current();

    int shadowStart = FastColor.ARGB32.color(0, 0, 0, 0);
    int shadowEnd = FastColor.ARGB32.color(100, 0, 0, 0);

    void render(GuiGraphics gui, ClientTeam.Teammate teammate);
}
