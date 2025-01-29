package com.clefal.teams.client.gui.util;

import com.clefal.teams.client.core.property.impl.Health;
import com.google.common.collect.HashMultimap;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VertexContainer {

    private HashMultimap<ResourceLocation, TextureBufferInfo> map = HashMultimap.create(10, 100);
    private List<IFillBufferInfo> fillBufferInfos = new ArrayList<>();

    public VertexContainer(){

    }

    public VertexContainer(int expectedKeys, int expectedValuesPerKey){
        this.map = HashMultimap.create(expectedKeys, expectedValuesPerKey);
    }

    public void putBliz(ResourceLocation resourceLocation, TextureBufferInfo bufferInfo){
        map.put(resourceLocation, bufferInfo);
    }

    public void putFill(IFillBufferInfo fillBufferInfo){
        fillBufferInfos.add(fillBufferInfo);
    }

    public void refresh(){
        this.map = HashMultimap.create(10, 100);
        this.fillBufferInfos = new ArrayList<>();
    }

    public void draw(MultiBufferSource bufferSource){
        //
        for (Map.Entry<ResourceLocation, Collection<TextureBufferInfo>> entry : this.map.asMap().entrySet()) {
            ResourceLocation key = entry.getKey();
            RenderType statusRenderType = StatusRenderType.getStatusRenderType(key.toString(), key);
            VertexConsumer buffer = bufferSource.getBuffer(statusRenderType);

            for (TextureBufferInfo bufferInfo : entry.getValue()) {
                bufferInfo.upload(buffer);
            }

        }
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.gui());
        if (!fillBufferInfos.isEmpty()){
            for (IFillBufferInfo fillBufferInfo : fillBufferInfos) {
                fillBufferInfo.upload(buffer);
            }
        }

        refresh();
    }

}