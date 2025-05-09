package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.S2CModPacket;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.utils.ClientHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;


public class S2CTeamAppliedPacket implements S2CModPacket {


    CompoundTag tag = new CompoundTag();

    public S2CTeamAppliedPacket(String name, UUID id) {
        tag.putString(S2CTeamPlayerDataPacket.NAME_KEY, name);
        tag.putUUID(S2CTeamPlayerDataPacket.ID_KEY, id);
    }

    public S2CTeamAppliedPacket(FriendlyByteBuf byteBuf) {
        tag = byteBuf.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeNbt(tag);
    }


    @Override
    public void handleClient() {
        String name = tag.getString(S2CTeamPlayerDataPacket.NAME_KEY);
        UUID id = tag.getUUID(S2CTeamPlayerDataPacket.ID_KEY);
        ClientHelper.addRequestToast(ClientTeam.INSTANCE.getName(), name, id);
    }
}
