package com.clefal.teams.client.core.property;

import com.clefal.teams.TeamsHUD;
import net.minecraft.resources.ResourceLocation;

public class Health extends RenderableProperty {
    private final float health;
    private final float maxHealth;
    private final ResourceLocation icon = TeamsHUD.id("textures/gui/health.png");
    public static final String KEY = "health";

    public Health(float health, float maxHealth) {
        this.health = health;
        this.maxHealth = maxHealth;
    }

    public static Health fromString(String str){
        String[] split = str.split("/");
        return new Health(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return icon;
    }

    @Override
    public String getRenderString() {
        return health + "/" + maxHealth;
    }

    @Override
    public String getIdentifier() {
        return KEY;
    }

}
