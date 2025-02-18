package com.clefal.teams.client.gui.toast;

import net.minecraft.network.chat.Component;

public class ToastConfigSave extends TeamToast{
    public ToastConfigSave(String team) {
        super(team);
    }

    @Override
    public Component title() {
        return Component.translatable("teams.menu.team_config.save.success_toast.title");
    }

    @Override
    public Component subTitle() {
        return Component.translatable("teams.menu.team_config.save.success_toast.subtitle");
    }
}
