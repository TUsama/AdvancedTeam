package com.clefal.teams.client.core.property.impl;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.HealthTemplate;
import com.clefal.teams.client.core.property.IRenderable;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import com.clefal.teams.config.ATConfig;
import com.clefal.teams.server.ModComponents;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

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


    @Override
    public void render(GuiGraphics gui, ClientTeam.Teammate teammate) {
        if (ATConfig.config.overlays.showHealth) {
            RenderSystem.enableBlend();
            PoseStack pose = gui.pose();
            pose.translate(0, oneEntryHeight, 0);
            pose.pushPose();
            if (this.isInDanger()){
                float randomFactor = random.nextFloat(-0.001f, 0.001f);
                gui.blit(this.getResourceLocation(), (int)(StatusOverlay.getRelativeWidth(randomFactor * 2)), (int)(StatusOverlay.getRelativeHeight(randomFactor)), 0, 0, 9, 9, 9, 9);
            } else {
                gui.blit(this.getResourceLocation(), 0, 0, 0, 0, 9, 9, 9, 9);
            }
            //container.map.put(hunger.getResourceLocation(), BufferInfo.of(pos.x, y, 0, 0, 9, 9, 9, 9, gui.pose().last().pose()));
            pose.translate(IRenderable.iconAndTextInterval, 0, 0);

            gui.fill(0, 0, (int) (barWidth * this.getTrackedBarLengthFactor()), (int) barHeight, this.getTrackedBarColor());
            gui.fillGradient(0, 0, (int) (barWidth * this.getTrackedBarLengthFactor()), (int) barHeight, shadowStart, shadowEnd);
            pose.translate(0, StatusOverlay.getRelativeHeight(0.005f), 0);
            gui.drawString(Minecraft.getInstance().font, ModComponents.literal(this.getRenderString()), 0, 0, ChatFormatting.WHITE.getColor());
            RenderSystem.disableBlend();
            pose.popPose();
        }
    }
}
