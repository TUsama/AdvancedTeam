package com.clefal.teams.client.core.property;

import net.minecraft.util.FastColor;

public abstract class HealthTemplate<SELF> extends RenderableTrackedFloatProperty<SELF> {

    private final Color whenLow = new Color(245, 31, 23);
    private final Color whenHigh = new Color(80, 231, 39);

    public HealthTemplate(Float health, Float maxHealth) {
        super(health, maxHealth);
    }

    @Override
    public String getRenderString() {
        return targetValue + "/" + maxValue;
    }


    @Override
    public void update() {
        if (currentValue < targetValue) {
            currentValue = Math.min(currentValue + INumberTracking.lerpTo(currentValue, targetValue), maxValue);
        } else {
            currentValue = targetValue;
        }

    }

    public boolean isInDanger() {
        return targetValue / maxValue < 0.2f;
    }

    @Override
    public int getTrackedBarColor() {
        double healthRatio = targetValue / maxValue;

        healthRatio = Math.max(0, Math.min(1, healthRatio));

        int red = (int) (whenHigh.red() * healthRatio + whenLow.red() * (1 - healthRatio));
        int green = (int) (whenHigh.green() * healthRatio + whenLow.green() * (1 - healthRatio));
        int blue = (int) (whenHigh.blue() * healthRatio + whenLow.blue() * (1 - healthRatio));

        return FastColor.ARGB32.color(190, red, green, blue);
    }

    @Override
    public SELF mergeWith(SELF old) {
        if (old == null) {
            return (SELF) this;
        } else {
            ((RenderableTrackedProperty) old).targetValue = this.currentValue;
            ((RenderableTrackedProperty) old).maxValue = this.maxValue;
            return old;
        }
    }
}
