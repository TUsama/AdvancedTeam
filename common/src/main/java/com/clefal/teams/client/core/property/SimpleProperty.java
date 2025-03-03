package com.clefal.teams.client.core.property;

import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import com.clefal.teams.client.core.property.impl.Health;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class SimpleProperty implements IProperty {

    public abstract ResourceLocation getResourceLocation();
    public abstract String getRenderString();

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;

        if (!(object instanceof Health health1)) return false;

        return new EqualsBuilder().append(getIdentifier(), health1.getIdentifier()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getIdentifier()).toHashCode();
    }
}
