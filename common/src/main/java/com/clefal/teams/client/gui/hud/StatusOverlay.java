package com.clefal.teams.client.gui.hud;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.config.ATClientConfig;
import com.clefal.teams.server.propertyhandler.HandlerManager;
import com.clefal.teams.server.propertyhandler.IPropertyClientHandler;
import com.clefal.teams.server.propertyhandler.PositionContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.List;

public class StatusOverlay {
    public static final StatusOverlay INSTANCE = new StatusOverlay();
    private final ResourceLocation FLAG = AdvancedTeam.id("textures/gui/flags.png");
    public boolean enabled = true;
    private final Minecraft client = Minecraft.getInstance();
    private final List<IPropertyClientHandler> handlers = HandlerManager.INSTANCE.getClientHandlers();
    private final VertexContainer container = new VertexContainer();

    public StatusOverlay() {
    }

    public void render(GuiGraphics graphics) {
        if (!ATClientConfig.config.overlays.enableStatusOverlay || !enabled) return;
        List<ClientTeam.Teammate> teammates = ClientTeam.INSTANCE.getTeammates();
        int shown = 0;
        RenderSystem.enableDepthTest();
        graphics.pose().pushPose();
        RenderSystem.enableBlend();
        PositionContext positionContext = PositionContext.fromFactor(0.03f, 0.025f, 0.15f, 0.026f);
        for (int i = 0; i < teammates.size() && shown < ATClientConfig.config.overlays.maxEntryAmount; ++i) {
            if (!AdvancedTeam.IN_DEV){
                if (client.player.getUUID().equals(teammates.get(i).id)) {
                    continue;
                }
            }
           renderStatus(graphics, teammates.get(i), positionContext);
            ++shown;
        }
        container.draw(graphics.bufferSource());
        //todo: this fill fix a weird bug that the last upload bufferinfo will ignore the DepthTest. but why?
        graphics.fill(0, 0, 1, 1, FastColor.ARGB32.color(0, 0, 0, 0));
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        graphics.pose().popPose();

    }

    private void renderStatus(GuiGraphics graphics, ClientTeam.Teammate teammate, PositionContext positionContext) {
        PoseStack pose = graphics.pose();
        //from left to right
        pose.pushPose();

        // Draw skin

        float scale = ATClientConfig.config.overlays.scale.get();
        pose.scale(scale, scale, 1.0f);

        pose.translate(ATClientConfig.config.overlays.originX, ATClientConfig.config.overlays.originY, 0);

        {
            PlayerFaceRenderer.draw(graphics, teammate.skin, 0, 0, 32);
        }

        // Draw name
        {
            pose.translate(40, 0, 0);
            graphics.drawString(client.font, Component.literal(teammate.name), 0, 0, ChatFormatting.WHITE.getColor());

            if (ClientTeam.INSTANCE.isLeader(teammate.id)){
                pose.pushPose();
                pose.translate(0, 0, 1);
                pose.translate(client.font.width(Component.literal(teammate.name)) + 10, 0, 0);
                graphics.blit(FLAG, 0, 0, 10, 10, 0, 0, 32, 32, 32, 64);
                pose.popPose();
            }
        }

        {
            for (var handler : this.handlers) {
                handler.onRender(graphics, container, teammate, positionContext);
            }
        }
        pose.popPose();

        graphics.pose().translate(0, ATClientConfig.config.overlays.entryInterval, 0);
    }

}
