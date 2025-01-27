package com.clefal.teams.client.core.property;

import com.clefal.teams.config.ATConfig;
import net.minecraft.util.Mth;
import org.joml.Math;

public interface ITracking<SELF> {

    SELF mergeWith(SELF old);
    void update();
    float getTrackedBarLengthFactor();
    int getTrackedBarColor();

    static float lerpTo(float from, float to){
        return Math.max((to - from) * ATConfig.config.overlays.barAnimationSpeed.get(), to * 0.01f);
    }
}
