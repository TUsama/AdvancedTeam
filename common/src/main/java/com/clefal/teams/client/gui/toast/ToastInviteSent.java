package com.clefal.teams.client.gui.toast;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

public class ToastInviteSent extends TeamToast {

    private String player;

    public ToastInviteSent(String team, String player) {
        super(team);
        this.player = player;
    }

    @Override
    public Component title() {
        return Component.translatable("teams.toast.invitesent");
    }

    @Override
    public Component subTitle() {
        return Component.translatable("teams.toast.invitesent.details", player);
    }
}
