package com.clefal.teams.client.core.property;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.server.propertyhandler.PositionContext;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;

import java.util.concurrent.ThreadLocalRandom;

public interface IPropertyRenderer {


    void render(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext);
}
