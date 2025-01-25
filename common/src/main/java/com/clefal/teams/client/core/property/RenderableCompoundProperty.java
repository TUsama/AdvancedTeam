package com.clefal.teams.client.core.property;

import java.util.HashMap;
import java.util.Map;

public abstract class RenderableCompoundProperty<SELF> extends Property implements IRenderable, ITracking<SELF> {
    public Map<String, ITracking<?>> propertyMap;

    public RenderableCompoundProperty(Map<String, ITracking<?>> propertyMap) {
        this.propertyMap = propertyMap;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public SELF mergeWith(SELF old) {
        if (old == null) {
            return (SELF) this;
        } else {
            propertyMap.entrySet().forEach(stringIPropertyEntry -> {

                ITracking value = stringIPropertyEntry.getValue();
                ITracking iTracking = ((RenderableCompoundProperty<?>) old).propertyMap.get(stringIPropertyEntry.getKey());
                ITracking o = (ITracking) value.mergeWith(iTracking);
                ((RenderableCompoundProperty<?>) old).propertyMap.put(stringIPropertyEntry.getKey(), o);


            });
            return old;
        }

    }
}
