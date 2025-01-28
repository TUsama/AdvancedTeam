package com.clefal.teams.network;

import com.clefal.teams.network.client.*;
import com.clefal.teams.network.server.*;
import com.clefal.teams.platform.Services;

public class Packets {


    public static void registerServerPackets() {
        Services.PLATFORM.registerServerMessage(C2STeamCreatePacket.class, C2STeamCreatePacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamRequestPacket.class, C2STeamRequestPacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamKickPacket.class, C2STeamKickPacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamLeavePacket.class, C2STeamLeavePacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamInvitePacket.class, C2STeamInvitePacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamJoinPacket.class, C2STeamJoinPacket::new);
        Services.PLATFORM.registerServerMessage(C2SPromotePacket.class, C2SPromotePacket::new);
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientMessage(S2CTeamPlayerDataPacket.class, S2CTeamPlayerDataPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamDataUpdatePacket.class, S2CTeamDataUpdatePacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamRequestedPacket.class, S2CTeamRequestedPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamClearPacket.class, S2CTeamClearPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamInvitedPacket.class, S2CTeamInvitedPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamInviteSentPacket.class, S2CTeamInviteSentPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamUpdatePacket.class, S2CTeamUpdatePacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamInitPacket.class, S2CTeamInitPacket::new);
        Services.PLATFORM.registerClientMessage(S2CPermissionChangePacket.class, S2CPermissionChangePacket::new);
    }
}
