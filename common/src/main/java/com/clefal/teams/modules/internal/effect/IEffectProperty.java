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

public interface IEffectProperty extends IProperty {
    @Override
    default ResourceLocation getResourceLocation(){
        throw new UnsupportedOperationException("can't invoke getResourceLocation on IEffectProperty!");
    }

    default List<TextureAtlasSprite> getTextureAtlas(){
        ArrayList<TextureAtlasSprite> textureAtlasSprites = new ArrayList<>();
        for (MobEffectInstance mobEffectInstance : getMobEffectInstance()) {
            Minecraft.getInstance().getMobEffectTextures().get(mobEffectInstance.getEffect());
        }
        return textureAtlasSprites;
    }

    List<MobEffectInstance> getMobEffectInstance();
}
