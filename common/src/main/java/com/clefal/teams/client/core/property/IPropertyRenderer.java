package com.clefal.teams.client.core.property;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;

import java.util.concurrent.ThreadLocalRandom;

public interface IPropertyRenderer {


    void render(GuiGraphics gui, ClientTeam.Teammate teammate);
}
