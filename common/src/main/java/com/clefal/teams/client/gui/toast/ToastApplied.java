package com.clefal.teams.client.gui.toast;

import com.clefal.teams.config.ATClientConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class ToastApplied extends RespondableTeamToast {

    public final UUID id;
    private final String name;

    public ToastApplied(String team, String name, UUID id) {
        super(team);
        this.name = name;
        this.id = id;
    }

    @Override
    public Visibility render(GuiGraphics graphics, ToastComponent manager, long startTime) {
        super.render(graphics, manager, startTime);

        if (getResponded()) {
            return Visibility.HIDE;
        }
        return startTime - getFirstDrawTime() < ATClientConfig.config.info.inviteToastShowSecond.get() * 1000L && team != null ? Visibility.SHOW : Visibility.HIDE;
    }

    public static ToastApplied of(String team, String name, UUID id){
        return new ToastApplied(team, name, id);
    }

    @Override
    public Component title() {
        return Component.translatable("teams.toast.requested", name);
    }
}
