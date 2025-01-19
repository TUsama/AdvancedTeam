package com.clefal.teams.client.core.property;

import com.clefal.teams.TeamsHUD;
import net.minecraft.resources.ResourceLocation;

public class Hunger extends RenderableProperty {
    private final float hunger;
    private final ResourceLocation icon = TeamsHUD.id("textures/gui/hunger.png");
    public static final String KEY = "hunger";


    public Hunger(float hunger) {
        this.hunger = hunger;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return icon;
    }

    @Override
    public String getRenderString() {
        return hunger + "/" + 20;
    }

    @Override
    public String getIdentifier() {
        return KEY;
    }
}
