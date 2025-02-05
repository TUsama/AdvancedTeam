package com.clefal.teams.mixin;

import com.clefal.teams.client.gui.components.ATEntryList;
import com.clefal.teams.mixinhelper.Hooker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = AbstractSelectionList.class)
public class AbstractSelectionListMixin {
    @Redirect(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 1))
    private void advancedTeam$invalidateFill1(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int color) {
        if (((AbstractSelectionList)((Object) this)) instanceof ATEntryList){
            Hooker.redirect1(instance, minX, minY, maxX, maxY, color);
        } else {
            instance.fill(minX, minY, maxX, maxY, color);
        }
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 2))
    private void advancedTeam$invalidateFill2(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int color) {
        if (((AbstractSelectionList)((Object) this)) instanceof ATEntryList){
            Hooker.redirect2(instance, minX, minY, maxX, maxY, color);
        } else {
            instance.fill(minX, minY, maxX, maxY, color);
        }
    }

    @Redirect(method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V", ordinal = 0))
    private void advancedTeam$invalidateFill(GuiGraphics instance, int minX, int minY, int maxX, int maxY, int color) {
        if (((AbstractSelectionList)((Object) this)) instanceof ATEntryList){
            Hooker.redirect0(instance, minX, minY, maxX, maxY, color);
        } else {
            instance.fill(minX, minY, maxX, maxY, color);
        }
    }


}
