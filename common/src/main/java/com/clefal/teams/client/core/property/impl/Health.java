package com.clefal.teams.client.core.property.impl;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.HealthTemplate;
import com.clefal.teams.client.gui.util.FillBufferInfo;
import com.clefal.teams.client.gui.util.FillGradientBufferInfo;
import com.clefal.teams.client.gui.util.TextureBufferInfo;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.config.ATClientConfig;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.server.propertyhandler.PositionContext;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import static com.clefal.teams.client.core.property.Constants.*;

public final class Health extends HealthTemplate<Health> {
    public static final String KEY = "health";
    private final ResourceLocation icon = AdvancedTeam.id("textures/gui/health.png");


    public Health(float health, float maxValue) {
        super(health, maxValue);
    }

    public static Health fromString(String str) {
        String[] split = str.split("/");
        return new Health(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return icon;
    }


    @Override
    public String getIdentifier() {
        return KEY;
    }




    public static class Renderer extends PropertyRenderer<Health> {
        private static Renderer renderer;

        public Renderer(@Nullable Health property) {
            super(property);
        }

        public static Renderer getRenderer(Health health) {
            if (renderer == null) {
                renderer = new Renderer(health);
            }
            return renderer;
        }

        @Override
        public void render(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext) {
            if (ATClientConfig.config.overlays.showHealth) {
                RenderSystem.enableBlend();
                PoseStack pose = gui.pose();
                pose.translate(0, positionContext.oneEntryHeight(), 0);

                pose.pushPose();
                Matrix4f matrix4f = new Matrix4f(gui.pose().last().pose());
                if (property.isInDanger()) {
                    float randomFactor = random.nextFloat(-0.001f, 0.001f);
                    //gui.blit(property.getResourceLocation(), (int) (PositionContext.getRelativeWidth(randomFactor * 2)), (int) (PositionContext.getRelativeHeight(randomFactor)), 0, 0, 9, 9, 9, 9);
                    container.putBliz(property.getResourceLocation(), TextureBufferInfo.of(PositionContext.getRelativeWidth(randomFactor * 2), PositionContext.getRelativeHeight(randomFactor), 0, 0, 9, 9, 9, 9, matrix4f));
                } else {
                    container.putBliz(property.getResourceLocation(), TextureBufferInfo.of(0, 0, 0, 0, 9, 9, 9, 9, matrix4f));
                    //gui.blit(property.getResourceLocation(), 0, 0, 0, 0, 9, 9, 9, 9);
                }

                pose.translate(positionContext.iconAndTextInterval(), 0, 0);
                Matrix4f matrix4f1 = new Matrix4f(pose.last().pose());
                float maxX = positionContext.iconAndTextInterval() + positionContext.barWidth() * property.getTrackedBarLengthFactor();
                float maxY = positionContext.barHeight();

                container.putFill(FillBufferInfo.fillOf(0, 0, maxX, maxY, -0.01f, property.getTrackedBarColor(), matrix4f1));
                //gui.fill(0, 0, (int) (getRelativeWidth(barWidth) * property.getTrackedBarLengthFactor()), ((int) getRelativeHeight(barHeight)), property.getTrackedBarColor());

                container.putFill(FillGradientBufferInfo.getShadow(0, 0, maxX, maxY, matrix4f1));

                //gui.fillGradient(0, 0, (int) (getRelativeWidth(barWidth) * property.getTrackedBarLengthFactor()), ((int) getRelativeHeight(barHeight)), shadowStart, shadowEnd);

                if (Screen.hasShiftDown()){
                    pose.translate(0, getRelativeHeight(0.005f), 10);
                    gui.drawString(Minecraft.getInstance().font, ModComponents.literal(property.getRenderString()), 0, 0, ChatFormatting.WHITE.getColor());
                }
                RenderSystem.disableBlend();
                pose.popPose();
            }
        }
    }

}
