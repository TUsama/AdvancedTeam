package com.clefal.teams.client.core.property;

import com.clefal.teams.client.core.IRenderableProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class RenderableProperty implements IRenderableProperty {
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
