package com.clefal.teams.client.gui.hud;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.Constants;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.config.ATConfig;
import com.clefal.teams.server.propertyhandler.HandlerManager;
import com.clefal.teams.server.propertyhandler.IPropertyClientHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class StatusOverlay {
    public static final StatusOverlay INSTANCE = new StatusOverlay();
    public boolean enabled = true;
    private final Minecraft client = Minecraft.getInstance();
    private final List<IPropertyClientHandler> handlers = HandlerManager.INSTANCE.getClientHandlers();
    private final VertexContainer container = new VertexContainer();

    public StatusOverlay() {
    }

    public void render(GuiGraphics graphics) {
        if (!ATConfig.config.overlays.enableStatusOverlay || !enabled) return;
        List<ClientTeam.Teammate> teammates = ClientTeam.INSTANCE.getTeammates();
        int shown = 0;
        graphics.pose().pushPose();
        for (int i = 0; i < teammates.size() && shown < 4; ++i) {
            if (client.player.getUUID().equals(teammates.get(i).id)) {
                continue;
            }
           renderStatus(graphics, teammates.get(i));
            ++shown;
        }
        graphics.pose().popPose();

    }

    private void renderStatus(GuiGraphics graphics, ClientTeam.Teammate teammate) {
        PoseStack pose = graphics.pose();
        //from left to right
        pose.pushPose();

        // Draw skin

        float scale = ATConfig.config.overlays.scale.get();
        pose.scale(scale, scale, 0);

        pose.translate(ATConfig.config.overlays.originX, ATConfig.config.overlays.originY, 0);

        {
            graphics.blit(teammate.skin, 0, 0, 32, 32, 32, 32);
        }

        // Draw name
        {
            pose.translate(40, 0, 0);
            graphics.drawString(client.font, Component.literal(teammate.name), 0, 0, ChatFormatting.WHITE.getColor());
        }

        {
            for (var handler : this.handlers) {
                //System.out.println(handler);
                handler.onRender(graphics, container, teammate);
            }
        }
        pose.popPose();

        graphics.pose().translate(0, ATConfig.config.overlays.entryInterval, 0);
    }

}
