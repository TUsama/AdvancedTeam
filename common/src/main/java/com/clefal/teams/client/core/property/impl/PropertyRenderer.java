package com.clefal.teams.client.core.property.impl;

import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import com.clefal.teams.client.core.property.IPropertyRenderer;

import javax.annotation.Nullable;

public abstract class PropertyRenderer<T extends IProperty> implements IPropertyRenderer {
    protected T property;

    public PropertyRenderer(@Nullable T property) {
        this.property = property;
    }

}
