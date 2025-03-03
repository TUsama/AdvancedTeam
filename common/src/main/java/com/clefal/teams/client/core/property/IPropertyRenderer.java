package com.clefal.teams.client.core.property;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.modules.internal.propertyhandler.PositionContext;
import net.minecraft.client.gui.GuiGraphics;

public interface IPropertyRenderer {


    void render(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext);
}
