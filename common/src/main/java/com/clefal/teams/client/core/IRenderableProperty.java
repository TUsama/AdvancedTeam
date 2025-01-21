package com.clefal.teams.client.core;

import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;

public interface IRenderableProperty {

    ResourceLocation getResourceLocation();
    String getRenderString();
    String getIdentifier();


}
