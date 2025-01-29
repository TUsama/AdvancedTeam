package com.clefal.teams.client.gui.toast;

import com.clefal.teams.config.ATConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;

public class ToastInvited extends RespondableTeamToast {

    public ToastInvited(String team) {
        super(team);
    }

    @Override
    public String title() {
        return I18n.get("teams.toast.invite", team);
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent manager, long startTime) {
        super.render(graphics, manager, startTime);

        if (getResponded()) {
            return Visibility.HIDE;
        }
        return startTime - getFirstDrawTime() < ATConfig.config.info.toastShowSecond.get() * 1000L * 5 && team != null ? Visibility.SHOW : Visibility.HIDE;
    }
}
