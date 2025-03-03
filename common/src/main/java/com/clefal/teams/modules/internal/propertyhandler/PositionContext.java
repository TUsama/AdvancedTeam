package com.clefal.teams.modules.internal.propertyhandler;

import com.clefal.teams.config.ATClientConfig;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import org.joml.Vector2f;

public record PositionContext(float oneEntryHeight, float iconAndTextInterval, float barWidth, float barHeight,
                              Vector2f origin, Vector2f finalEffectPosition) {
    public static final int iconSize = 16;
    public static final int interval = 2;
    public static final int nameInterval = 10;

    public static PositionContext fromOrigin(Vector2f origin) {
        float oneEntryHeight = getRelativeHeight(0.03f);
        float iconAndTextInterval = getRelativeWidth(0.025f);
        float barWidth = getRelativeWidth(0.15f);
        float barHeight = getRelativeHeight(0.026f);

        return new PositionContext(oneEntryHeight, iconAndTextInterval, barWidth, barHeight, origin, new Vector2f(iconAndTextInterval + barWidth + 5, barHeight));
    }

    public static float getRelativeWidth(float factor) {
        return Minecraft.getInstance().getWindow().getGuiScaledWidth() * factor;
    }

    public static float getRelativeHeight(float factor) {
        return Minecraft.getInstance().getWindow().getGuiScaledHeight() * factor;
    }

    public PoseStack setupEffectPosition(PoseStack poseStack) {
        setupNamePosition(poseStack);
        poseStack.translate(finalEffectPosition().x, finalEffectPosition().y, 0);
        return poseStack;
    }

    public PositionContext updateOrigin(Vector2f origin) {
        return fromOrigin(origin);
    }

    public PoseStack setupInitialPosition(PoseStack poseStack) {
        poseStack.setIdentity();
        float scale = ATClientConfig.config.overlays.scale.get();
        poseStack.scale(scale, scale, 1.0f);
        poseStack.translate(origin.x, origin.y, 0);
        return poseStack;
    }

    public PoseStack setupNamePosition(PoseStack poseStack) {
        setupInitialPosition(poseStack);
        poseStack.translate(getPlayerHeadIconSize() + PositionContext.nameInterval, 0, 0);
        return poseStack;
    }

    public int getPlayerHeadIconSize() {
        return (int) (this.barWidth() / 3);
    }
}
