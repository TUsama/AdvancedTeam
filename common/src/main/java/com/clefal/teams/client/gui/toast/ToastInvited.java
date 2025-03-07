package com.clefal.teams.client.gui.toast;

import com.clefal.teams.config.ATClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class ToastInvited extends RespondableTeamToast {

    public ToastInvited(String team) {
        super(team);
    }

    @Override
    public Component title() {
        return Component.translatable("teams.toast.invite", team);
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent manager, long startTime) {
        super.render(graphics, manager, startTime);

        if (getResponded()) {
            return Visibility.HIDE;
        }
        return startTime - getFirstDrawTime() < ATClientConfig.config.info.inviteToastShowSecond.get() * 1000L && team != null ? Visibility.SHOW : Visibility.HIDE;
    }
}
