package com.clefal.teams.compat.property;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.IRenderable;
import com.clefal.teams.client.core.property.RenderableCompoundProperty;
import com.clefal.teams.client.core.property.RenderableTrackedProperty;
import com.clefal.teams.client.gui.hud.StatusOverlay;
import com.clefal.teams.server.ModComponents;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class MNSHealthResource extends RenderableCompoundProperty<MNSHealthResource> {
    public static final String KEY = "MNSHealthResource";
    private MNSHealth health;
    private MNSMagicShield magicShield;
    private final int healthColor = FastColor.ARGB32.color(190, 80, 231, 39);
    private final int magicShieldColor = FastColor.ARGB32.color(190, 66, 241, 224);

    public MNSHealthResource(@NotNull MNSHealth health, @NotNull MNSMagicShield magicShield) {
        super(new HashMap<>(Map.of(health.getIdentifier(), health, magicShield.getIdentifier(), magicShield)));
        this.health = health;
        this.magicShield = magicShield;
    }

    public boolean isInDanger() {
        //System.out.println((health.currentValue + magicShield.currentValue)  / (health.maxValue + magicShield.maxValue));
        return (health.currentValue + magicShield.currentValue)  / (health.maxValue + magicShield.maxValue) < 0.2f;
    }

    @Override
    public void render(GuiGraphics gui, ClientTeam.Teammate teammate) {
        float current = health.currentValue + magicShield.currentValue;
        float max = health.maxValue + magicShield.maxValue;
        float healthResourceRatio = Math.max(0, Math.min(1, current / max));
        float currentBarLength = barWidth * healthResourceRatio;
        float healthFactor = health.currentValue / current;

        {
            PoseStack pose = gui.pose();
            pose.translate(0, oneEntryHeight, 0);
            pose.pushPose();
            if (this.isInDanger()){
                float randomFactor = random.nextFloat(-0.005f, 0.005f);
                gui.blit(this.getResourceLocation(), (int)(StatusOverlay.getRelativeWidth(randomFactor)), (int)(StatusOverlay.getRelativeHeight(randomFactor)), 0, 0, 9, 9, 9, 9);
            } else {
                gui.blit(this.getResourceLocation(), 0, 0, 0, 0, 9, 9, 9, 9);
            }

            pose.translate(IRenderable.iconAndTextInterval, 0, 0);
            //health
            gui.fill(0, 0, (int) (currentBarLength * healthFactor), (int) barHeight, health.getTrackedBarColor());
            //magic shield
            gui.fill((int) (currentBarLength * healthFactor), 0, (int) currentBarLength, (int) barHeight, magicShieldColor);
            gui.fillGradient(0, 0, (int) (currentBarLength), (int) barHeight, shadowStart, shadowEnd);
            pose.translate(0, StatusOverlay.getRelativeHeight(0.005f), 0);
            if (Screen.hasShiftDown()){
                gui.drawString(Minecraft.getInstance().font, ModComponents.literal(this.getRenderString()), 0, 0, ChatFormatting.WHITE.getColor());
            }
            pose.popPose();
        }
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return health.maxValue >= magicShield.maxValue ? health.getResourceLocation() : magicShield.getResourceLocation();
    }

    @Override
    public String getRenderString() {
        return (health.targetValue.intValue() + magicShield.targetValue.intValue()) + "/" + (health.maxValue.intValue() + magicShield.maxValue.intValue());
    }

    @Override
    public String getIdentifier() {
        return KEY;
    }

    @Override
    public void update() {
        health.update();
        magicShield.update();
    }
}
