package com.clefal.teams.client.gui.toast;

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

    public static ToastApplied of(String team, String name, UUID id){
        return new ToastApplied(team, name, id);
    }

    @Override
    public Component title() {
        return Component.translatable("teams.toast.requested", name);
    }
}
