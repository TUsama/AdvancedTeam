package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.List;
import com.clefal.teams.client.gui.menu.invite.TeamsInvitePlayerScreen;
import com.clefal.teams.client.gui.toast.*;
import com.clefal.teams.client.gui.util.PlayerWithSkin;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;

import java.util.UUID;
import java.util.function.Supplier;

@UtilityClass
public class Helper {

    public void addToast(Supplier<Toast> toast){
        Minecraft.getInstance().getToasts().addToast(toast.get());
    }
    public void addToast(Toast toast){
        Minecraft.getInstance().getToasts().addToast(toast);
    }

    public void addRequestToast(String team, String name, UUID id){
        addToast(ToastRequested.of(team, name, id));
    }

    public void addInviteToast(String team){
        addToast(new ToastInvited(team));
    }

    public void addJoinOrLeaveToast(S2CTeamUpdatePacket. Action action, String team, String name, boolean local){
        switch (action) {
            case JOINED -> addToast(new ToastJoin(team, name, local));
            case LEFT -> addToast(new ToastLeave(team, name, local));
        }
    }

    public void addInviteSentToast(String team, String player){
        addToast(new ToastInviteSent(team, player));
    }


    public void tryInitScreenEntryList(List<PlayerWithSkin> names){
        if (Minecraft.getInstance().screen instanceof TeamsInvitePlayerScreen screen) {
            screen.entryList.updateList(names.toJavaList());
        }
    }
}
