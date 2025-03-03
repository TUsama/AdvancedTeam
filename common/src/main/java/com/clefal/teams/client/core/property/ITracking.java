package com.clefal.teams.client.core.property;

public interface ITracking<SELF> {
    SELF mergeWith(SELF old);
}
