package com.clefal.teams.client.core.property.impl;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.IRenderable;
import com.clefal.teams.client.core.property.Property;
import com.clefal.teams.config.ATConfig;
import com.clefal.teams.server.ModComponents;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class Hunger extends Property implements IRenderable {
    private final float hunger;
    private final ResourceLocation icon = AdvancedTeam.id("textures/gui/hunger.png");
    public static final String KEY = "hunger";


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

    @Override
    public void render(GuiGraphics gui, ClientTeam.Teammate teammate) {
        if (ATConfig.config.overlays.showHunger){
            PoseStack pose = gui.pose();
            pose.translate(0, IRenderable.oneEntryHeight, 0);
            pose.pushPose();
            gui.blit(this.getResourceLocation(), 0, 0, 0, 0, 9, 9, 9, 9);
            //container.map.put(hunger.getResourceLocation(), BufferInfo.of(pos.x, y, 0, 0, 9, 9, 9, 9, gui.pose().last().pose()));
            pose.translate(iconAndTextInterval, 0, 0);
            gui.drawString(Minecraft.getInstance().font, ModComponents.literal(this.getRenderString()), 0, 0, ChatFormatting.WHITE.getColor());
            pose.popPose();
        }

    }
}
