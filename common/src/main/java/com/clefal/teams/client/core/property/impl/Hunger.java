package com.clefal.teams.client.core.property.impl;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.RenderableProperty;
import com.clefal.teams.client.gui.util.TextureBufferInfo;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.config.ATClientConfig;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.modules.internal.propertyhandler.PositionContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public class Hunger extends RenderableProperty {
    public static final String KEY = "hunger";
    private final float hunger;
    private final ResourceLocation icon = AdvancedTeam.id("textures/gui/hunger.png");


    public Hunger(float hunger) {
        this.hunger = hunger;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return icon;
    }

    @Override
    public String getRenderString() {
        return hunger + "/" + 20;
    }

    @Override
    public String getIdentifier() {
        return KEY;
    }



    public static class Renderer extends PropertyRenderer<Hunger> {
        private static Renderer renderer;

        public Renderer(@Nullable Hunger property) {
            super(property);
        }

        public static Renderer getRenderer(Hunger property) {
            if (renderer == null) {
                renderer = new Renderer(property);
            }
            return renderer;
        }

        @Override
        public void render(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext) {
            if (ATClientConfig.config.overlays.showHunger) {
                PoseStack pose = gui.pose();
                pose.translate(0, positionContext.oneEntryHeight(), 0);
                pose.pushPose();
                container.putBliz(property.getResourceLocation(), TextureBufferInfo.of(0, 0, 0, 0, 9, 9, 9, 9, new Matrix4f(pose.last().pose())));
                //gui.blit(property.getResourceLocation(), 0, 0, 0, 0, 9, 9, 9, 9);
                //container.map.put(hunger.getResourceLocation(), BufferInfo.of(pos.x, y, 0, 0, 9, 9, 9, 9, gui.pose().last().pose()));
                pose.translate(positionContext.iconAndTextInterval(), 0, 0);
                gui.drawString(Minecraft.getInstance().font, ModComponents.literal(property.getRenderString()), 0, 0, ChatFormatting.WHITE.getColor());
                pose.popPose();
            }
        }
    }
}
