package com.clefal.teams.server.propertyhandler;

import net.minecraft.client.Minecraft;

public record PositionContext(float oneEntryHeight, float iconAndTextInterval, float barWidth, float barHeight) {

    public static PositionContext fromFactor(float oneEntryHeight, float iconAndTextInterval, float barWidth, float barHeight){
        return new PositionContext(getRelativeHeight(oneEntryHeight), getRelativeWidth(iconAndTextInterval), getRelativeWidth(barWidth), getRelativeHeight(barHeight));
    }
    public static float getRelativeWidth(float factor){
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() * factor;
    }

    public static float getRelativeHeight(float factor){
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() * factor;
    }
}
