package com.clefal.teams.client.gui.util;

import com.clefal.nirvana_lib.client.render.rendertype.RenderTypeCreator;
import com.clefal.teams.mixin.AccessorRenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class StatusRenderType extends RenderType {

    public StatusRenderType(String $$0, VertexFormat $$1, VertexFormat.Mode $$2, int $$3, boolean $$4, boolean $$5, Runnable $$6, Runnable $$7){
        super($$0, $$1, $$2, $$3, $$4, $$5, $$6, $$7);
    }

    public static Map<ResourceLocation, RenderType> cache = new HashMap<>();

    public static RenderType getRenderType(ResourceLocation resourceLocation){
        return cache.computeIfAbsent(resourceLocation, RenderTypeCreator.gui);
    }


}
