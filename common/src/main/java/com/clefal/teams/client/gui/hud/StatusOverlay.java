package com.clefal.teams.client.gui.hud;

import com.clefal.teams.TeamsHUD;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.config.ATConfig;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.server.propertyhandler.HandlerManager;
import com.clefal.teams.server.propertyhandler.IPropertyHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2f;

import java.util.List;

public class StatusOverlay {

    public boolean enabled = true;
    private final Minecraft client;
    private final List<IPropertyHandler> handlers = HandlerManager.INSTANCE.getHandlers();
    private final VertexContainer container = new VertexContainer();

    public StatusOverlay() {
        this.client = Minecraft.getInstance();
    }

    public void render(GuiGraphics graphics) {
        if (!ATConfig.config.overlays.enableStatusHUD || !enabled) return;
        var offsetY = 0;
        List<ClientTeam.Teammate> teammates = ClientTeam.INSTANCE.getTeammates();
        int shown = 0;
        Vector2f pos = new Vector2f(0, 0);
        pos.add(client.getWindow().getGuiScaledWidth() * 0.003f, 0);
        for (int i = 0; i < teammates.size() && shown < 4; ++i) {
            if (client.player.getUUID().equals(teammates.get(i).id)) {
                continue;
            }
            pos.add(0, (float) client.getWindow().getGuiScaledHeight() / 4 - 5 + offsetY);
            pos.set(renderStatus(graphics, teammates.get(i), pos));
            ++shown;

        }
        this.container.draw(graphics.bufferSource());
    }

    private Vector2f renderStatus(GuiGraphics graphics, ClientTeam.Teammate teammate, Vector2f pos) {
        // Dont render dead players
        //Clefal: why?
        //if (teammate.getHealth() <= 0) return;

        for (IPropertyHandler handler : this.handlers) {
            pos = handler.onRender(graphics, container, teammate, pos);
        }


        // Draw skin
        graphics.pose().pushPose();
        graphics.pose().scale(0.5F, 0.5F, 0.5F);
        graphics.blit(teammate.skin, ((int) pos.x) + 4, (int) (client.getWindow().getGuiScaledHeight() / 2 - 34 + 2 * pos.y), 32, 32, 32, 32);
        graphics.pose().popPose();

        // Draw name
        graphics.drawString(client.font, Component.literal(teammate.name), (int) Math.round(client.getWindow().getGuiScaledWidth() * 0.002) + 20, ((int) (pos.y - 15)), ChatFormatting.WHITE.getColor());

        // Update count & offset
        pos.add(0, 46);
        return pos;
    }

}
