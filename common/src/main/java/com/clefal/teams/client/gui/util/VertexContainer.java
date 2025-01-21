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

    public VertexContainer(){

    }

    public VertexContainer(int expectedKeys, int expectedValuesPerKey){
        this.map = HashMultimap.create(expectedKeys, expectedValuesPerKey);
    }

    public void refresh(){
        this.map = HashMultimap.create(10, 100);
    }

    public void draw(MultiBufferSource bufferSource){
        //System.out.println("map has " + this.map.size());
        for (Map.Entry<ResourceLocation, Collection<BufferInfo>> entry : this.map.asMap().entrySet()) {
            ResourceLocation key = entry.getKey();
            RenderType statusRenderType = StatusRenderType.getStatusRenderType(key.toString(), key);
            VertexConsumer buffer = bufferSource.getBuffer(statusRenderType);

            for (BufferInfo bufferInfo : entry.getValue()) {
                bufferInfo.upload(buffer);
            }

        }
    }

}