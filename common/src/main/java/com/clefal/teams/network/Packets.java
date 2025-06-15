package com.clefal.teams.network;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.network.client.*;
import com.clefal.teams.network.client.config.S2CTeamConfigBooleanPacket;
import com.clefal.teams.network.server.*;
import com.clefal.teams.network.server.config.C2STeamConfigSavePacket;

public class Packets {


    public static void registerServerPackets() {
        NetworkUtils.registerPacket(C2STeamCreatePacket::new);
        NetworkUtils.registerPacket(C2STeamApplyingPacket::new);
        NetworkUtils.registerPacket(C2STeamKickPacket::new);
        NetworkUtils.registerPacket(C2STeamLeavePacket::new);
        NetworkUtils.registerPacket(C2STeamInvitePacket::new);
        NetworkUtils.registerPacket(C2STeamJoinPacket::new);
        NetworkUtils.registerPacket(C2SPromotePacket::new);
        NetworkUtils.registerPacket(C2SAskNoTeamPlayerWithSkinPacket::new);
        NetworkUtils.registerPacket(C2STeamConfigSavePacket::new);
        NetworkUtils.registerPacket(C2SAskRequestPlayerWithSkinPacket::new);
        NetworkUtils.registerPacket(C2SAcceptApplicationPacket::new);

    }

    public static void registerClientPackets() {
        NetworkUtils.registerPacket(S2CTeamPlayerDataPacket::new);
        NetworkUtils.registerPacket(S2CTeamDataUpdatePacket::new);
        NetworkUtils.registerPacket(S2CTeamAppliedPacket::new);
        NetworkUtils.registerPacket(S2CTeamClearPacket::new);
        NetworkUtils.registerPacket(S2CTeamInvitedPacket::new);
        NetworkUtils.registerPacket(S2CTeamInviteSentPacket::new);
        NetworkUtils.registerPacket(S2CTeamUpdatePacket::new);
        NetworkUtils.registerPacket(S2CTeamInitPacket::new);
        NetworkUtils.registerPacket(S2CPermissionUpdatePacket::new);
        NetworkUtils.registerPacket(S2CSyncRenderMatPacket::new);
        NetworkUtils.registerPacket(S2CReturnPlayerWithSkinPacket::new);
        NetworkUtils.registerPacket(S2CTeamConfigBooleanPacket.Public::new);
        NetworkUtils.registerPacket(S2CTeamConfigBooleanPacket.EveryoneCanInvite::new);
    }
}
