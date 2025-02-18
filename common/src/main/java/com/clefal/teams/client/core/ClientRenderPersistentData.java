package com.clefal.teams.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientRenderPersistentData {
    public static ClientRenderPersistentData getInstance(){
        if (Delegate.INSTANCE == null) Delegate.INSTANCE = new ClientRenderPersistentData();
        return Delegate.INSTANCE;
    }

    public final List<String> invitations = new ArrayList<>();
    public final List<String> applications = new ArrayList<>();


    public void clear(){
        this.invitations.clear();
        applications.clear();
    }

    private static class Delegate{
        public static ClientRenderPersistentData INSTANCE = new ClientRenderPersistentData();

    }
}
