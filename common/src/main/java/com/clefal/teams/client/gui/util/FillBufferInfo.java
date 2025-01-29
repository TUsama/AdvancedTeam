package com.clefal.teams.client.gui.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

public record FillBufferInfo(float minX, float minY, float maxX, float maxY, float z, int packedColor,
                             Matrix4f matrix4f) implements IFillBufferInfo {


    public static FillBufferInfo fillOf(float minX, float minY, float maxX, float maxY, float z, int color, Matrix4f matrix4f) {
        return new FillBufferInfo(minX, minY, maxX, maxY, z, color, matrix4f);
    }

    @Override
    public void upload(VertexConsumer consumer) {
        consumer.vertex(matrix4f, minX, minY, z).color(packedColor).endVertex();
        consumer.vertex(matrix4f, minX, maxY, z).color(packedColor).endVertex();
        consumer.vertex(matrix4f, maxX, maxY, z).color(packedColor).endVertex();
        consumer.vertex(matrix4f, maxX, minY, z).color(packedColor).endVertex();
    }
}
