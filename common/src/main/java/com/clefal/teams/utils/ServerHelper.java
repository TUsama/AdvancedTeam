package com.clefal.teams.utils;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.network.client.S2CSyncRenderMatPacket;
import com.clefal.teams.network.client.S2CTeamInvitedPacket;
import com.clefal.teams.server.Invitation;
import lombok.experimental.UtilityClass;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
@UtilityClass
public class ServerHelper {
    public void addInv(Map<String, Invitation> invitations, Invitation invitation, ServerPlayer player){
        invitations.put(invitation.teamName, invitation);
        NetworkUtils.sendToClient(new S2CSyncRenderMatPacket(invitation.teamName, S2CSyncRenderMatPacket.Action.ADD, S2CSyncRenderMatPacket.Type.INVITATION), player);
        NetworkUtils.sendToClient(new S2CTeamInvitedPacket(invitation.teamName), player);
    }
}
