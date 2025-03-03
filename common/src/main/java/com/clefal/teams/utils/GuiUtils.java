package com.clefal.teams.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import lombok.experimental.UtilityClass;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.Objects;

@UtilityClass
public class GuiUtils {

    public void renderDuration(GuiGraphics gui, int duration, PoseStack pose, int i, int iconSize) {
        Component formatDuration;
        if (duration >= 20 * 60){
            formatDuration =  Component.translatable("teams.effect_format.minute", duration / (20 * 60));
        } else {
            formatDuration = Component.translatable("teams.effect_format.second", duration / 20);
        }

        String string = formatDuration.getString();
        StringBuilder numberPart = new StringBuilder();
        boolean hasWord = false;
        for (char c : string.toCharArray()) {
            if (Character.isDigit(c)) {
                numberPart.append(c);
            } else {
                hasWord = true;
                break;
            }
        }
        String number = numberPart.toString();
        pose.pushPose();
        float scale = 1.0f;

        pose.scale(scale,  scale, 1);

        float antiScale = 1.0F / scale;

        float textWidthMinus = (float)Minecraft.getInstance().font.width(number) / 2.0F * scale;
        Objects.requireNonNull(Minecraft.getInstance().font);
        float textHeightMinus = (9.0F * scale / 2.0F);
        float xp = ((i + (float) iconSize / 2)) - textWidthMinus;
        float yp = iconSize - textHeightMinus;
        float xf = xp * antiScale;
        float yf = yp * antiScale;
        pose.translate(xf, yf, 0);
        gui.drawString(Minecraft.getInstance().font, number, 0, 0, ChatFormatting.WHITE.getColor());

        if (hasWord){
            pose.translate(textWidthMinus, 0, 0);
            gui.drawString(Minecraft.getInstance().font, string.replace(number, ""), -1, 0, ChatFormatting.WHITE.getColor());
        }
        pose.popPose();
    }


}
