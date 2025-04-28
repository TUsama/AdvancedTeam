package com.clefal.teams.network;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.network.client.*;
import com.clefal.teams.network.client.config.S2CTeamConfigBooleanPacket;
import com.clefal.teams.network.server.*;
import com.clefal.teams.network.server.config.C2STeamConfigSavePacket;
import com.clefal.teams.platform.Services;

public class Packets {


    public static void registerServerPackets() {
        NetworkUtils.registerServerMessage(C2STeamCreatePacket.class, C2STeamCreatePacket::new);
        NetworkUtils.registerServerMessage(C2STeamApplyingPacket.class, C2STeamApplyingPacket::new);
        NetworkUtils.registerServerMessage(C2STeamKickPacket.class, C2STeamKickPacket::new);
        NetworkUtils.registerServerMessage(C2STeamLeavePacket.class, C2STeamLeavePacket::new);
        NetworkUtils.registerServerMessage(C2STeamInvitePacket.class, C2STeamInvitePacket::new);
        NetworkUtils.registerServerMessage(C2STeamJoinPacket.class, C2STeamJoinPacket::new);
        NetworkUtils.registerServerMessage(C2SPromotePacket.class, C2SPromotePacket::new);
        NetworkUtils.registerServerMessage(C2SAskNoTeamPlayerWithSkinPacket.class, C2SAskNoTeamPlayerWithSkinPacket::new);
        NetworkUtils.registerServerMessage(C2STeamConfigSavePacket.class, C2STeamConfigSavePacket::new);
        NetworkUtils.registerServerMessage(C2SAskRequestPlayerWithSkinPacket.class, C2SAskRequestPlayerWithSkinPacket::new);
        NetworkUtils.registerServerMessage(C2SAcceptApplicationPacket.class, C2SAcceptApplicationPacket::new);

    }

    public static void registerClientPackets() {
        NetworkUtils.registerClientMessage(S2CTeamPlayerDataPacket.class, S2CTeamPlayerDataPacket::new);
        NetworkUtils.registerClientMessage(S2CTeamDataUpdatePacket.class, S2CTeamDataUpdatePacket::new);
        NetworkUtils.registerClientMessage(S2CTeamAppliedPacket.class, S2CTeamAppliedPacket::new);
        NetworkUtils.registerClientMessage(S2CTeamClearPacket.class, S2CTeamClearPacket::new);
        NetworkUtils.registerClientMessage(S2CTeamInvitedPacket.class, S2CTeamInvitedPacket::new);
        NetworkUtils.registerClientMessage(S2CTeamInviteSentPacket.class, S2CTeamInviteSentPacket::new);
        NetworkUtils.registerClientMessage(S2CTeamUpdatePacket.class, S2CTeamUpdatePacket::new);
        NetworkUtils.registerClientMessage(S2CTeamInitPacket.class, S2CTeamInitPacket::new);
        NetworkUtils.registerClientMessage(S2CPermissionUpdatePacket.class, S2CPermissionUpdatePacket::new);
        NetworkUtils.registerClientMessage(S2CInvitationPacket.class, S2CInvitationPacket::new);
        NetworkUtils.registerClientMessage(S2CReturnPlayerWithSkinPacket.class, S2CReturnPlayerWithSkinPacket::new);
        NetworkUtils.registerClientMessage(S2CTeamConfigBooleanPacket.Public.class, S2CTeamConfigBooleanPacket.Public::new);
        NetworkUtils.registerClientMessage(S2CTeamConfigBooleanPacket.EveryoneCanInvite.class, S2CTeamConfigBooleanPacket.EveryoneCanInvite::new);
    }
}
