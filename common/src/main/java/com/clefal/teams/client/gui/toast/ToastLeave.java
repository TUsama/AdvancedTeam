package com.clefal.teams.client.gui.toast;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class ToastLeave extends TeamToast {

    private String name;
    private boolean local;

    public ToastLeave(String team, String name, boolean local) {
        super(team);
        this.name = name;
        this.local = local;
    }

    @Override
    public Component title() {
        return local ? Component.translatable("teams.toast.leave") : Component.translatable("teams.toast.left");
    }

    @Override
    public Component subTitle() {
        return local ? Component.translatable("teams.toast.leave.details", team) : Component.translatable("teams.toast.left.details", name);
    }
}
