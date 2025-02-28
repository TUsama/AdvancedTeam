package com.clefal.teams.client.core.property;

import com.clefal.teams.config.ATClientConfig;
import org.joml.Math;

public interface INumberTracking<SELF> extends ITracking<SELF>{


    void update();
    float getTrackedBarLengthFactor();
    int getTrackedBarColor();

    static float lerpTo(float from, float to){
        return Math.max((to - from) * ATClientConfig.config.overlays.barAnimationSpeed.get(), to * 0.01f);
    }
}
