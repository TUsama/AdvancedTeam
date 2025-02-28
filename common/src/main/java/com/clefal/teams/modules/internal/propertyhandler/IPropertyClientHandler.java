package com.clefal.teams.modules.internal.propertyhandler;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.client.ClientRegisterPropertyRendererEvent;
import net.minecraft.client.gui.GuiGraphics;

public interface IPropertyClientHandler {
    void onReadProperty(ClientReadPropertyEvent event);
    void onRegisterRenderer(ClientRegisterPropertyRendererEvent event);
    void onRender(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext);

}
