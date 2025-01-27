package com.clefal.teams.compat.mine_and_slash.property;

import com.clefal.teams.client.core.property.HealthTemplate;
import com.robertx22.mine_and_slash.gui.overlays.BarGuiType;
import net.minecraft.resources.ResourceLocation;

public class MNSHealth extends HealthTemplate<MNSHealth> {
    public static final String KEY = "MNSHealth";

    public MNSHealth(float health, float maxHealth) {
        super(health, maxHealth);
    }

    public static MNSHealth fromString(String str) {
        String[] split = str.split("/");
        return new MNSHealth(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return BarGuiType.HEALTH.getIcon(null, null);
    }

    @Override
    public String getIdentifier() {
        return KEY;
    }

}
