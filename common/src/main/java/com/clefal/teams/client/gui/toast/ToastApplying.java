package com.clefal.teams.client.gui.toast;

import net.minecraft.network.chat.Component;

public class ToastApplying extends TeamToast {

    public ToastApplying(String team) {
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
