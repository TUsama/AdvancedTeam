package com.clefal.teams.client.gui.util;

import com.google.common.collect.HashMultimap;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Map;

public class VertexContainer {

    public HashMultimap<ResourceLocation, BufferInfo> map = HashMultimap.create(10, 100);

    public void refresh(){
        this.map = HashMultimap.create(10, 100);
    }

    public void draw(MultiBufferSource bufferSource){
        for (Map.Entry<ResourceLocation, Collection<BufferInfo>> entry : this.map.asMap().entrySet()) {
            ResourceLocation key = entry.getKey();
            RenderType skillTreeRenderType = StatusRenderType.getStatusRenderType(key.toString(), key);
            VertexConsumer buffer = bufferSource.getBuffer(skillTreeRenderType);

            for (BufferInfo bufferInfo : entry.getValue()) {
                bufferInfo.upload(buffer);
            }

        }
    }

}