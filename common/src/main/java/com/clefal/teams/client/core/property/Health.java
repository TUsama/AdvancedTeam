package com.clefal.teams.client.core.property;

import com.clefal.teams.TeamsHUD;
import net.minecraft.resources.ResourceLocation;

public class Health extends RenderableProperty {
    private final float health;
    private final ResourceLocation icon = TeamsHUD.id("textures/gui/health.png");
    public static final String KEY = "health";

    public Health(float health) {
        this.health = health;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return icon;
    }

    @Override
    public String getRenderString() {
        return String.valueOf(health);
    }

    @Override
    public String getIdentifier() {
        return KEY;
    }


}
