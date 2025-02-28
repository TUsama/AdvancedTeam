package com.clefal.teams.modules.internal.effect.handlers;

import com.clefal.teams.client.core.property.ITracking;
import com.clefal.teams.client.core.property.RenderableProperty;
import com.clefal.teams.modules.internal.effect.IEffectProperty;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class VanillaPotionEffectProperty extends RenderableProperty implements IEffectProperty, ITracking<VanillaPotionEffectProperty> {
    public static final String KEY = "vanilla_potion_effect";
    private List<MobEffectInstance> instance;

    public VanillaPotionEffectProperty(List<MobEffectInstance> instance) {
        this.instance = instance;
    }

    @Override
    public String getRenderString() {
        return "";
    }

    @Override
    public String getIdentifier() {
        return KEY;
    }

    @Override
    public List<MobEffectInstance> getMobEffectInstance() {
        return instance;
    }

    @Override
    public VanillaPotionEffectProperty mergeWith(VanillaPotionEffectProperty old) {
        if (old == null) {
            return this;
        } else {
            old.instance = this.instance;
            return old;
        }
    }

}
