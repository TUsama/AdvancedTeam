package com.clefal.teams.client.core.property;

import net.minecraft.client.Minecraft;
import net.minecraft.util.FastColor;

import java.util.concurrent.ThreadLocalRandom;

public class Constants {

    public static float oneEntryHeight = 0.03f;
    public static float iconAndTextInterval = 0.025f;
    public static float barWidth = 0.15f;
    public static float barHeight = 0.026f;
    public static ThreadLocalRandom random = ThreadLocalRandom.current();
    public static int shadowStart = FastColor.ARGB32.color(0, 0, 0, 0);
    public static int shadowEnd = FastColor.ARGB32.color(100, 0, 0, 0);

    public static float getRelativeWidth(float factor){
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() * factor;
    }

    public static float getRelativeHeight(float factor){
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() * factor;
    }
}
