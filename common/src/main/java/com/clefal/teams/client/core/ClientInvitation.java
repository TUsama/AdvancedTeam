package com.clefal.teams.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientInvitation {
    public static ClientInvitation INSTANCE = new ClientInvitation();

    public final List<String> invitations = new ArrayList<>();


    public void clear(){
        this.invitations.clear();
    }
}
