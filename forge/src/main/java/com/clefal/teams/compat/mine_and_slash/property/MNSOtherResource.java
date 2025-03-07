package com.clefal.teams.compat.mine_and_slash.property;

import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.core.property.INumberTracking;
import com.clefal.teams.client.core.property.RenderableTrackedProperty;
import com.clefal.teams.client.core.property.impl.PropertyRenderer;
import com.clefal.teams.client.gui.util.FillBufferInfo;
import com.clefal.teams.client.gui.util.FillGradientBufferInfo;
import com.clefal.teams.client.gui.util.VertexContainer;
import com.clefal.teams.modules.internal.propertyhandler.PositionContext;
import com.google.common.collect.ImmutableMap;
import com.robertx22.mine_and_slash.saveclasses.unit.ResourceType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

public class MNSOtherResource extends RenderableTrackedProperty<MNSOtherResource, Float> {
    public static Function<ResourceType, String> identifier = type1 -> "MNS" + type1;
    private final static Map<String, Integer> colorMap = ImmutableMap.of(
            identifier.apply(ResourceType.blood), FastColor.ARGB32.color(190, 221, 16, 16),
            identifier.apply(ResourceType.mana), FastColor.ARGB32.color(190, 78, 161, 241),
            identifier.apply(ResourceType.energy), FastColor.ARGB32.color(190, 93, 236, 149)
    );
    private final ResourceType type;
    private final ResourceLocation icon = AdvancedTeam.id("textures/gui/health.png");

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
            currentValue = Math.min(currentValue + INumberTracking.lerpTo(currentValue, targetValue), maxValue);
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
            old.maxValue = this.maxValue;
            return old;
        }
    }


    public static class Renderer extends PropertyRenderer<MNSOtherResource> {

        public Renderer(@Nullable MNSOtherResource property) {
            super(property);
        }

        public static Renderer getRenderer(MNSOtherResource other) {
            return new Renderer(other);
        }

        @Override
        public void render(GuiGraphics gui, VertexContainer container, ClientTeam.Teammate teammate, PositionContext positionContext) {
            float width = positionContext.barWidth() / 2;
            float height = positionContext.barHeight() / 3;
            Matrix4f matrix4f = new Matrix4f(gui.pose().last().pose());
            float trackedBarLengthFactor = property.getTrackedBarLengthFactor();

            container.putFill(FillBufferInfo.fillOf(0, 0, width * trackedBarLengthFactor, height, -0.01f, colorMap.get(property.getIdentifier()), matrix4f));
            container.putFill(FillGradientBufferInfo.getShadow(0, 0, width * trackedBarLengthFactor, height, matrix4f));

            /*gui.fill(0, 0, (int) (width * factor), ((int) (height)), colorMap.get(property.getIdentifier()));
            gui.fillGradient(0, 0, (int) (width * factor), ((int) (height)), Constants.shadowStart, Constants.shadowEnd);*/
        }

    }
}
