package com.clefal.teams.client.gui.toast;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class ToastJoin extends TeamToast {

    private String name;
    private boolean local;

    public ToastJoin(String team, String name, boolean local) {
        super(team);
        this.name = name;
        this.local = local;
    }

    @Override
    public Component title() {
        return local ? Component.translatable("teams.toast.join") : Component.translatable("teams.toast.joined");
    }

    @Override
    public Component subTitle() {
        return local ? Component.translatable("teams.toast.join.details", team) : Component.translatable("teams.toast.joined.details", name);
    }
}
