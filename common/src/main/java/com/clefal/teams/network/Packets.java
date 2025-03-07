package com.clefal.teams.network;

import com.clefal.teams.network.client.*;
import com.clefal.teams.network.client.config.S2CTeamConfigBooleanPacket;
import com.clefal.teams.network.server.*;
import com.clefal.teams.network.server.config.C2STeamConfigSavePacket;
import com.clefal.teams.platform.Services;

public class Packets {


    public static void registerServerPackets() {
        Services.PLATFORM.registerServerMessage(C2STeamCreatePacket.class, C2STeamCreatePacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamApplyingPacket.class, C2STeamApplyingPacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamKickPacket.class, C2STeamKickPacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamLeavePacket.class, C2STeamLeavePacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamInvitePacket.class, C2STeamInvitePacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamJoinPacket.class, C2STeamJoinPacket::new);
        Services.PLATFORM.registerServerMessage(C2SPromotePacket.class, C2SPromotePacket::new);
        Services.PLATFORM.registerServerMessage(C2SAskNoTeamPlayerWithSkinPacket.class, C2SAskNoTeamPlayerWithSkinPacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamConfigSavePacket.class, C2STeamConfigSavePacket::new);
        Services.PLATFORM.registerServerMessage(C2SAskRequestPlayerWithSkinPacket.class, C2SAskRequestPlayerWithSkinPacket::new);
        Services.PLATFORM.registerServerMessage(C2SAcceptApplicationPacket.class, C2SAcceptApplicationPacket::new);

    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientMessage(S2CTeamPlayerDataPacket.class, S2CTeamPlayerDataPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamDataUpdatePacket.class, S2CTeamDataUpdatePacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamAppliedPacket.class, S2CTeamAppliedPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamClearPacket.class, S2CTeamClearPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamInvitedPacket.class, S2CTeamInvitedPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamInviteSentPacket.class, S2CTeamInviteSentPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamUpdatePacket.class, S2CTeamUpdatePacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamInitPacket.class, S2CTeamInitPacket::new);
        Services.PLATFORM.registerClientMessage(S2CPermissionUpdatePacket.class, S2CPermissionUpdatePacket::new);
        Services.PLATFORM.registerClientMessage(S2CInvitationPacket.class, S2CInvitationPacket::new);
        Services.PLATFORM.registerClientMessage(S2CReturnPlayerWithSkinPacket.class, S2CReturnPlayerWithSkinPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamConfigBooleanPacket.Public.class, S2CTeamConfigBooleanPacket.Public::new);
        Services.PLATFORM.registerClientMessage(S2CTeamConfigBooleanPacket.EveryoneCanInvite.class, S2CTeamConfigBooleanPacket.EveryoneCanInvite::new);
    }
}
