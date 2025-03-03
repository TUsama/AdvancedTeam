package com.clefal.teams.compat.mine_and_slash.property;

import com.clefal.teams.client.core.property.ITracking;
import com.clefal.teams.modules.internal.effect.EffectProperty;

import java.util.List;

public class MNSStatusEffect extends EffectProperty<MNSStatusEffectData> implements ITracking<MNSStatusEffect> {
    public static final String KEY = "MNSEffect";

    public MNSStatusEffect(List<MNSStatusEffectData> effects) {
        super(effects);
    }


    @Override
    public String getIdentifier() {
        return KEY;
    }

    @Override
    public MNSStatusEffect mergeWith(MNSStatusEffect old) {
        if (old == null) {
            return this;
        } else {
            old.effects = super.effects;
            return old;
        }
    }
}
