package com.clefal.teams.network;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.AdvancedTeam;
import com.clefal.teams.network.client.*;
import com.clefal.teams.network.client.config.S2CTeamConfigBooleanPacket;
import com.clefal.teams.network.server.*;
import com.clefal.teams.network.server.config.C2STeamConfigSavePacket;

public class Packets {


    public static void registerServerPackets() {
        NetworkUtils.registerPacket(C2STeamCreatePacket.class, C2STeamCreatePacket::new);
        NetworkUtils.registerPacket(C2STeamApplyingPacket.class, C2STeamApplyingPacket::new);
        NetworkUtils.registerPacket(C2STeamKickPacket.class, C2STeamKickPacket::new);
        NetworkUtils.registerPacket(C2STeamLeavePacket.class, C2STeamLeavePacket::new);
        NetworkUtils.registerPacket(C2STeamInvitePacket.class, C2STeamInvitePacket::new);
        NetworkUtils.registerPacket(C2STeamJoinPacket.class, C2STeamJoinPacket::new);
        NetworkUtils.registerPacket(C2SPromotePacket.class, C2SPromotePacket::new);
        NetworkUtils.registerPacket(C2SAskNoTeamPlayerWithSkinPacket.class, C2SAskNoTeamPlayerWithSkinPacket::new);
        NetworkUtils.registerPacket(C2STeamConfigSavePacket.class, C2STeamConfigSavePacket::new);
        NetworkUtils.registerPacket(C2SAskRequestPlayerWithSkinPacket.class, C2SAskRequestPlayerWithSkinPacket::new);
        NetworkUtils.registerPacket(C2SAcceptApplicationPacket.class, C2SAcceptApplicationPacket::new);

    }

    public static void registerClientPackets() {
        NetworkUtils.registerPacket(S2CTeamPlayerDataPacket.class, S2CTeamPlayerDataPacket::new);
        NetworkUtils.registerPacket(S2CTeamDataUpdatePacket.class, S2CTeamDataUpdatePacket::new);
        NetworkUtils.registerPacket(S2CTeamAppliedPacket.class, S2CTeamAppliedPacket::new);
        NetworkUtils.registerPacket(S2CTeamClearPacket.class, S2CTeamClearPacket::new);
        NetworkUtils.registerPacket(S2CTeamInvitedPacket.class, S2CTeamInvitedPacket::new);
        NetworkUtils.registerPacket(S2CTeamInviteSentPacket.class, S2CTeamInviteSentPacket::new);
        NetworkUtils.registerPacket(S2CTeamUpdatePacket.class, S2CTeamUpdatePacket::new);
        NetworkUtils.registerPacket(S2CTeamInitPacket.class, S2CTeamInitPacket::new);
        NetworkUtils.registerPacket(S2CPermissionUpdatePacket.class, S2CPermissionUpdatePacket::new);
        NetworkUtils.registerPacket(S2CSyncRenderMatPacket.class, S2CSyncRenderMatPacket::new);
        NetworkUtils.registerPacket(S2CReturnPlayerWithSkinPacket.class, S2CReturnPlayerWithSkinPacket::new);
        NetworkUtils.registerPacket(S2CTeamConfigBooleanPacket.Public.class, S2CTeamConfigBooleanPacket.Public::new);
        NetworkUtils.registerPacket(S2CTeamConfigBooleanPacket.EveryoneCanInvite.class, S2CTeamConfigBooleanPacket.EveryoneCanInvite::new);
    }
}
