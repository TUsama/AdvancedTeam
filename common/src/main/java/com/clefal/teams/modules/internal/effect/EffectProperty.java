package com.clefal.teams.modules.internal.effect;

import com.clefal.teams.modules.internal.propertyhandler.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;

public abstract class EffectProperty<T> implements IProperty {
    public List<T> effects;

    public EffectProperty(List<T> effects) {
        this.effects = effects;
    }


}
