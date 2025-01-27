package com.clefal.teams.compat.property;

import com.clefal.teams.client.core.property.HealthTemplate;
import com.robertx22.mine_and_slash.gui.overlays.BarGuiType;
import net.minecraft.resources.ResourceLocation;

public class MNSMagicShield extends HealthTemplate<MNSMagicShield> {
    public static final String KEY = "MNSMagicShield";

    public MNSMagicShield(float shield, float maxShield) {
        super(shield, maxShield);
    }

    public static MNSMagicShield fromString(String str) {
        try {
            String[] split = str.split("/");
            return new MNSMagicShield(Float.parseFloat(split[0]), Float.parseFloat(split[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResourceLocation getResourceLocation() {
        return BarGuiType.MAGIC_SHIELD.getIcon(null, null);
    }

    @Override
    public String getIdentifier() {
        return KEY;
    }

}
