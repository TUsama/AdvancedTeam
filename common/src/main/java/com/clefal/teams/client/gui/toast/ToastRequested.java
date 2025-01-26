package com.clefal.teams.client.gui.toast;

import net.minecraft.client.resources.language.I18n;

import java.util.UUID;

public class ToastRequested extends RespondableTeamToast {

    public final UUID id;
    private final String name;

    public ToastRequested(String team, String name, UUID id) {
        super(team);
        this.name = name;
        this.id = id;
    }

    public static ToastRequested of(String team, String name, UUID id){
        return new ToastRequested(team, name, id);
    }

    @Override
    public String title() {
        return I18n.get("teams.toast.requested", name);
    }
}
