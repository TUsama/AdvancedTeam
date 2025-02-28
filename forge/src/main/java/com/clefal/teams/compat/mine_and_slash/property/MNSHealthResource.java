package com.clefal.teams.compat.mine_and_slash.property;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.RenderableCompoundProperty;
import com.clefal.teams.client.core.property.Constants;
import com.clefal.teams.client.gui.util.FillBufferInfo;
import com.clefal.teams.client.gui.util.FillGradientBufferInfo;
import com.clefal.teams.client.gui.util.TextureBufferInfo;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.server.ModComponents;
import com.clefal.teams.modules.internal.propertyhandler.PositionContext;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

import static com.clefal.teams.client.core.property.Constants.*;

public class MNSHealthResource extends RenderableCompoundProperty<MNSHealthResource> {
    public static final String KEY = "MNSHealthResource";
    private final int healthColor = FastColor.ARGB32.color(190, 80, 231, 39);
    private final int magicShieldColor = FastColor.ARGB32.color(190, 66, 241, 224);
    private MNSHealth health;
    private MNSMagicShield magicShield;

    public MNSHealthResource(@NotNull MNSHealth health, @NotNull MNSMagicShield magicShield) {
        super(new HashMap<>(Map.of(health.getIdentifier(), health, magicShield.getIdentifier(), magicShield)));
        this.health = health;
        this.magicShield = magicShield;
    }

    public boolean isInDanger() {
        return (health.currentValue + magicShield.currentValue) / (health.maxValue + magicShield.maxValue) < 0.2f;
    }

    @Override
    public void render(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext) {
        float current = health.currentValue + magicShield.currentValue;
        float max = health.maxValue + magicShield.maxValue;
        float healthResourceRatio = Math.max(0, Math.min(1, current / max));
        //it can't be 1.0f if I don't add this.
        healthResourceRatio = healthResourceRatio > 0.99f ? 1.0f : healthResourceRatio;
        var WholeWidth = Constants.getRelativeWidth(barWidth);
        float currentBarLength = WholeWidth * healthResourceRatio;
        float healthFactor = Math.min(1.0f, health.currentValue / current);
        {
            PoseStack pose = gui.pose();
            pose.translate(0, positionContext.oneEntryHeight(), 0);

            Matrix4f matrix4f = new Matrix4f(gui.pose().last().pose());
            pose.pushPose();
            if (this.isInDanger()) {
                float randomFactor = Constants.random.nextFloat(-0.005f, 0.005f);
                container.putBliz(this.getResourceLocation(), TextureBufferInfo.of(PositionContext.getRelativeWidth(randomFactor * 2), PositionContext.getRelativeHeight(randomFactor), 0, 0, 9, 9, 9, 9, matrix4f));

                //gui.blit(this.getResourceLocation(), (int) (Constants.getRelativeWidth(randomFactor)), (int) (getRelativeHeight(randomFactor)), 0, 0, 9, 9, 9, 9);
            } else {
                container.putBliz(this.getResourceLocation(), TextureBufferInfo.of(0, 0, 0, 0, 9, 9, 9, 9, matrix4f));
            }

            pose.translate(positionContext.iconAndTextInterval(), 0, 0);
            Matrix4f matrix4f1 = new Matrix4f(gui.pose().last().pose());
            //health

            container.putFill(FillBufferInfo.fillOf(0, 0, currentBarLength * healthFactor, positionContext.barHeight(), 0.002f, health.getTrackedBarColor(), matrix4f1));


            //gui.fill(0, 0, (int) (currentBarLength * healthFactor), ((int) getRelativeHeight(barHeight)), health.getTrackedBarColor());
            //magic shield
            container.putFill(FillBufferInfo.fillOf(currentBarLength * healthFactor, 0, currentBarLength, positionContext.barHeight(), 0.002f, magicShieldColor, matrix4f1));
            //gui.fill((int) (currentBarLength * healthFactor), 0, (int) currentBarLength, ((int) getRelativeHeight(barHeight)), magicShieldColor);
            container.putFill(FillGradientBufferInfo.getShadow(0, 0, currentBarLength, positionContext.barHeight(), matrix4f1));
            //gui.fillGradient(0, 0, (int) (currentBarLength), ((int) getRelativeHeight(barHeight)), Constants.shadowStart, Constants.shadowEnd);

            //System.out.println("2: " + pose);
            if (Screen.hasShiftDown()) {
                pose.translate(0, getRelativeHeight(0.005f), 10);
                Minecraft.getInstance().font.drawInBatch(ModComponents.literal(this.getRenderString()), 0, 0, ChatFormatting.WHITE.getColor(), false, new Matrix4f(pose.last().pose()), gui.bufferSource(), Font.DisplayMode.NORMAL, FastColor.ARGB32.color(0, 255, 255, 255), 15728880);
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
