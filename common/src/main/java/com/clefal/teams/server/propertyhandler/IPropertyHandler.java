package com.clefal.teams.server.propertyhandler;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.event.client.ClientReadPropertyEvent;
import com.clefal.teams.event.server.ServerGatherPropertyEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Vector2f;

public interface IPropertyHandler {
    void onGather(ServerGatherPropertyEvent event);
    void onRead(ClientReadPropertyEvent event);
    Vector2f onRender(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate,Vector2f pos);
}
