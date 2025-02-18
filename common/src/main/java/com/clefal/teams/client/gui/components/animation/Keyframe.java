package com.clefal.teams.client.gui.components.animation;

import lombok.Getter;
import net.minecraft.world.phys.Vec3;

@Getter
public class Keyframe extends Frame {
    int duration;
    Easings.Easing easing;

    public Keyframe(Vec3 position, Vec3 rotation, Vec3 scale, int duration, Easings.Easing easing) {
        super(position, rotation, scale);
        this.duration = duration;
        this.easing = easing;
    }

    public Keyframe copy() {
        return new Keyframe(position, rotation, scale, duration, easing);
    }

}

