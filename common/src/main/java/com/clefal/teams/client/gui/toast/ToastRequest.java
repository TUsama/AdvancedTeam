package com.clefal.teams.client.gui.toast;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class ToastRequest extends TeamToast {

    public ToastRequest(String team) {
        super(team);
    }

    @Override
    public Component title() {
        return Component.translatable("teams.toast.request");
    }

    @Override
    public Component subTitle() {
        return Component.translatable("teams.toast.request.details", team);
    }
}
