package com.clefal.teams.client.core.property;

public abstract class RenderableTrackedFloatProperty<SELF> extends RenderableTrackedProperty<SELF, Float>{
    public RenderableTrackedFloatProperty(Float currentValue, Float maxValue) {
        super(currentValue, maxValue);
    }
}
