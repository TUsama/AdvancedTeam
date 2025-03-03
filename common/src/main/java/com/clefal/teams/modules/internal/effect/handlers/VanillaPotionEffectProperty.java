package com.clefal.teams.modules.internal.effect.handlers;

import com.clefal.teams.client.core.property.ITracking;
import com.clefal.teams.modules.internal.effect.EffectProperty;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class VanillaPotionEffectProperty extends EffectProperty<MobEffectInstance> implements ITracking<VanillaPotionEffectProperty> {
    public static final String KEY = "vanilla_potion_effect";

    public VanillaPotionEffectProperty(List<MobEffectInstance> instance) {
        super(instance);
    }

    @Override
    public String getIdentifier() {
        return KEY;
    }

    public List<MobEffectInstance> getMobEffectInstance() {
        return super.effects;
    }

    @Override
    public VanillaPotionEffectProperty mergeWith(VanillaPotionEffectProperty old) {
        if (old == null) {
            return this;
        } else {
            old.effects = super.effects;
            return old;
        }
    }

}
