package com.clefal.teams.server.propertyhandler;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import net.minecraft.client.gui.GuiGraphics;

public interface IPropertyClientHandler {
    void onRead(ClientReadPropertyEvent event);
    void onRender(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate);

}
