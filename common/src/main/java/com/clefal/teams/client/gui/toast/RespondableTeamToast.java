package com.clefal.teams.client.gui.toast;

import com.clefal.teams.client.keybind.TeamsKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public abstract class RespondableTeamToast extends TeamToast {

    private boolean responded = false;

    public RespondableTeamToast(String team) {
        super(team);
    }

    public void respond() {
        responded = true;
    }
    protected boolean getResponded(){
        return responded;
    }

    @Override
    public Component subTitle() {
        String rejectKey = TeamsKeys.REJECT.getLocalizedName();
        String acceptKey = TeamsKeys.ACCEPT.getLocalizedName();
        return Component.translatable("teams.toast.respond", Component.literal(rejectKey).withStyle(ChatFormatting.RED), Component.literal(acceptKey).withStyle(ChatFormatting.RED));
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent manager, long startTime) {
        if (responded) {
            return Visibility.HIDE;
        }
        return super.render(graphics, manager, startTime);
    }
}
