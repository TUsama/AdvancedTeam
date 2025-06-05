package com.clefal.teams.client.gui.hud;

import com.clefal.nirvana_lib.utils.DevUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.config.ATClientConfig;
import com.clefal.teams.modules.internal.HandlerManager;
import com.clefal.teams.modules.internal.propertyhandler.IPropertyClientHandler;
import com.clefal.teams.modules.internal.propertyhandler.PositionContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.joml.Vector2f;

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
        PositionContext positionContext = PositionContext.fromOrigin(new Vector2f(ATClientConfig.config.overlays.originX, ATClientConfig.config.overlays.originY));

        for (int i = 0; i < teammates.size() && shown < ATClientConfig.config.overlays.maxEntryAmount; ++i) {
            ClientTeam.Teammate teammate = teammates.get(i);
            if (!DevUtils.isInDev() && client.player.getUUID().equals(teammate.id)){
                continue;
            }
            PoseStack pose = graphics.pose();
            //from left to right
            pose.pushPose();

            // Draw skin
            positionContext.setupInitialPosition(pose);

            {
                PlayerFaceRenderer.draw(graphics, teammate.skin, 0, 0, positionContext.getPlayerHeadIconSize());
                if (ClientTeam.INSTANCE.isLeader(teammate.id)){
                pose.pushPose();
                int size = 14;
                pose.translate(positionContext.getPlayerHeadIconSize() - size, -3, 0);
                graphics.blit(FLAG, 0, 0, size, size, 0, 0, 32, 32, 32, 64);
                pose.popPose();
            }

            }

            // Draw name
            {
                positionContext.setupNamePosition(pose);
                graphics.drawString(client.font, Component.literal(teammate.name), 0, 0, ChatFormatting.WHITE.getColor());

            }

            {
                for (var handler : this.handlers) {
                    handler.onRender(graphics, container, teammate, positionContext);
                }
            }
            positionContext = positionContext.updateOrigin(positionContext.origin().add(0, positionContext.getPlayerHeadIconSize() + ATClientConfig.config.overlays.entryInterval));
            pose.popPose();

            ++shown;
        }
        container.draw(graphics.bufferSource());
        //todo: this fill fix a weird bug that the last upload bufferinfo will ignore the DepthTest. but why?
        graphics.fill(0, 0, 1, 1, FastColor.ARGB32.color(0, 0, 0, 0));
        RenderSystem.disableDepthTest();
        RenderSystem.disableBlend();
        graphics.pose().popPose();

    }

}
