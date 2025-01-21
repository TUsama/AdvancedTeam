package com.clefal.teams.mixin;

import com.clefal.teams.AdvancedTeamsHUDClient;
import com.clefal.teams.client.gui.hud.CompassOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PlayerTabOverlay.class)
public class TabHudMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "render", at = @At("STORE"), ordinal = 9)
    private int onRenderTabList(int p) {
        if (CompassOverlay.INSTANCE.isShowing()) {
            float scaledHeight = minecraft.getWindow().getGuiScaledHeight();
            return (int) (scaledHeight * 0.01) + 12 + 16;
        }
        return p;
    }

}
