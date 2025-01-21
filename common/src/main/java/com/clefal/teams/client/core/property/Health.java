package com.clefal.teams.client.core.property;

import com.clefal.teams.AdvancedTeam;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;

public class Health extends RenderableProperty implements IHasBar{
    private final float health;
    private final float maxHealth;
    private final ResourceLocation icon = AdvancedTeam.id("textures/gui/health.png");
    public static final String KEY = "health";
    private final TextColor color = TextColor.fromRgb(FastColor.ARGB32.color(255, 242, 66, 28));

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

    @Override
    public TextColor getBarColor() {
        return color;
    }

}
