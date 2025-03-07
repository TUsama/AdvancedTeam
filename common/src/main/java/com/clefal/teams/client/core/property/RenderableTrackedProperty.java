package com.clefal.teams.client.core.property;

public abstract class RenderableTrackedProperty<SELF, T extends Number> extends RenderableProperty implements INumberTracking<SELF> {
    public T currentValue;
    public T targetValue;
    public T maxValue;

    public RenderableTrackedProperty(T currentValue, T maxValue) {
        this.currentValue = currentValue;
        this.targetValue = currentValue;
        this.maxValue = maxValue;
    }

    @Override
    public float getTrackedBarLengthFactor(){
        float healthRatio = currentValue.floatValue() / maxValue.floatValue();
        update();
        return Math.max(0, Math.min(1, healthRatio));
    }


}
