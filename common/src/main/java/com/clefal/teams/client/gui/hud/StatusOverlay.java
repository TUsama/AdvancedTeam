package com.clefal.teams.client.gui.hud;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.config.ATConfig;
import com.clefal.teams.server.propertyhandler.HandlerManager;
import com.clefal.teams.server.propertyhandler.IPropertyHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;

import java.util.List;

public class StatusOverlay {
    public static final StatusOverlay INSTANCE = new StatusOverlay();
    public boolean enabled = true;
    private final Minecraft client = Minecraft.getInstance();
    private final List<IPropertyHandler> handlers = HandlerManager.INSTANCE.getHandlers();
    private final VertexContainer container = new VertexContainer();

    public StatusOverlay() {
    }

    public void render(GuiGraphics graphics) {
        if (!ATConfig.config.overlays.enableStatusHUD || !enabled) return;
        List<ClientTeam.Teammate> teammates = ClientTeam.INSTANCE.getTeammates();
        int shown = 0;

        for (int i = 0; i < teammates.size() && shown < 4; ++i) {
            /*if (client.player.getUUID().equals(teammates.get(i).id)) {
                continue;
            }*/

           renderStatus(graphics, teammates.get(i));
            ++shown;
        }
        this.container.draw(graphics.bufferSource());
        this.container.refresh();
    }

    private void renderStatus(GuiGraphics graphics, ClientTeam.Teammate teammate) {
        PoseStack pose = graphics.pose();
        //from left to right
        pose.pushPose();

        // Draw skin

        float scale = 0.5f;
        pose.scale(scale, scale, 0);

        pose.translate(getRelativeWidth(0.05f / scale), getRelativeHeight(0.5f / scale), 0);

        {
            graphics.blit(teammate.skin, 0, 0, 32, 32, 32, 32);
        }

        // Draw name
        {
            pose.translate(40, 0, 0);
            graphics.drawString(client.font, Component.literal(teammate.name), 0, 0, ChatFormatting.WHITE.getColor());
        }

        {
            for (IPropertyHandler handler : this.handlers) {
                handler.onRender(graphics, container, teammate);
            }
        }
        pose.popPose();

    }

    public static float getRelativeWidth(float factor){
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() * factor;
    }
    public static float getRelativeHeight(float factor){
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() * factor;
    }

}
