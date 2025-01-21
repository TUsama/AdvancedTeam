package com.clefal.teams.client.gui.util;

import com.clefal.teams.mixin.AccessorRenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class StatusRenderType extends RenderType {

    public StatusRenderType(String $$0, VertexFormat $$1, VertexFormat.Mode $$2, int $$3, boolean $$4, boolean $$5, Runnable $$6, Runnable $$7){
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
    }

    public static RenderType getStatusRenderType(String name, ResourceLocation texture) {
        RenderType.CompositeState renderTypeState = RenderType.CompositeState.builder()
                .setShaderState(RenderStateShard.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                .setTextureState(new TextureStateShard(texture, false, false))
                .setTransparencyState(new TransparencyStateShard("normal_blend", RenderSystem::enableBlend, RenderSystem::disableBlend))
                .setLightmapState(LIGHTMAP)
                .createCompositeState(false);
        return AccessorRenderType.teams$create(name, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 1536, false, true, renderTypeState);
    }
}
