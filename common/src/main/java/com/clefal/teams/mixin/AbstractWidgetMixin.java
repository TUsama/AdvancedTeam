package com.clefal.teams.mixin;

import com.clefal.teams.mixinhelper.IAbstractWidgetDuck;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = AbstractWidget.class)
public abstract class AbstractWidgetMixin implements IAbstractWidgetDuck {


    @Shadow protected int height;

    @Shadow public abstract void setWidth(int pWidth);

    @Override
    public void advancedTeam$setHeight(int pHeight) {
        this.height = pHeight;
    }

    public void advancedTeam$setWidthAndHeight(int pWidth, int pHeight) {
        setWidth(pWidth);
        advancedTeam$setHeight(pHeight);
    }
}
