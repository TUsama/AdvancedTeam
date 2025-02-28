package com.clefal.teams.client.core.property;

import java.util.Map;

public abstract class RenderableCompoundProperty<SELF> extends Property implements IPropertyRenderer, INumberTracking<SELF> {
    public Map<String, INumberTracking<?>> propertyMap;

    public RenderableCompoundProperty(Map<String, INumberTracking<?>> propertyMap) {
        this.propertyMap = propertyMap;
    }

    @Override
    public float getTrackedBarLengthFactor() {
        return 0;
    }

    @Override
    public int getTrackedBarColor() {
        return 0;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public SELF mergeWith(SELF old) {
        if (old == null) {
            return (SELF) this;
        } else {
            propertyMap.entrySet().forEach(stringIPropertyEntry -> {

                INumberTracking value = stringIPropertyEntry.getValue();
                INumberTracking iTracking = ((RenderableCompoundProperty<?>) old).propertyMap.get(stringIPropertyEntry.getKey());
                INumberTracking o = (INumberTracking) value.mergeWith(iTracking);
                ((RenderableCompoundProperty<?>) old).propertyMap.put(stringIPropertyEntry.getKey(), o);


            });
            return old;
        }

    }
}
