package com.clefal.teams.compat.property;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.IRenderable;
import com.clefal.teams.client.core.property.ITracking;
import com.clefal.teams.client.core.property.RenderableTrackedProperty;
import com.google.common.collect.ImmutableMap;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourceType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

import java.util.Map;
import java.util.function.Function;

public class MNSOtherResource extends RenderableTrackedProperty<MNSOtherResource, Float> {
    public final static float barWidth = IRenderable.barWidth / 2;
    public final static float barHeight = IRenderable.barHeight / 3;
    public static Function<ResourceType, String> identifier = type1 -> "MNS" + type1;
    private final ResourceType type;
    private final ResourceLocation icon = AdvancedTeam.id("textures/gui/health.png");
    private final static Map<String, Integer> colorMap = ImmutableMap.of(
            identifier.apply(ResourceType.blood), FastColor.ARGB32.color(190, 221, 16, 16),
            identifier.apply(ResourceType.mana), FastColor.ARGB32.color(190, 78, 161, 241),
            identifier.apply(ResourceType.energy), FastColor.ARGB32.color(190, 93, 236, 149)
    );

    public MNSOtherResource(Float currentValue, Float maxValue, ResourceType type) {
        super(currentValue, maxValue);
        this.type = type;
    }

    public static MNSOtherResource fromNetworkString(String str) {
        String[] split = str.split(":");
        ResourceType type = ResourceType.valueOf(split[0]);
        String[] split1 = split[1].split("/");
        return new MNSOtherResource(Float.parseFloat(split1[0]), Float.parseFloat(split1[1]), type);
    }

    public String getNetworkString() {
        return type + ":" + getRenderString();
    }

    @Override
    public void update() {
        if (currentValue < targetValue) {
            currentValue = Math.min(currentValue + ITracking.lerpTo(currentValue, targetValue), maxValue);
        } else {
            currentValue = targetValue;
        }
    }

    @Override
    public int getTrackedBarColor() {
        return 0;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return icon;
    }

    @Override
    public String getRenderString() {
        return targetValue + "/" + maxValue;
    }

    @Override
    public String getIdentifier() {
        return identifier.apply(type);
    }


    @Override
    public MNSOtherResource mergeWith(MNSOtherResource old) {
        if (old == null) {
            return this;
        } else {
            old.targetValue = this.currentValue;
            return old;
        }
    }

    @Override
    public void render(GuiGraphics gui, ClientTeam.Teammate teammate) {

        float factor = targetValue / maxValue;
        gui.fill(0, 0, (int) (barWidth * factor), (int) barHeight, colorMap.get(this.getIdentifier()));
        gui.fillGradient(0, 0, (int) (barWidth * factor), (int) barHeight, shadowStart, shadowEnd);
    }
}
