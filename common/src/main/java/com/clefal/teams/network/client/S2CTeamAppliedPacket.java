package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;
import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.utils.ClientHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;


public class S2CTeamAppliedPacket implements S2CModPacket<S2CTeamAppliedPacket> {


    CompoundTag tag = new CompoundTag();

    public S2CTeamAppliedPacket(String name, UUID id) {
        tag.putString(S2CTeamPlayerDataPacket.NAME_KEY, name);
        tag.putUUID(S2CTeamPlayerDataPacket.ID_KEY, id);
    }

    public S2CTeamAppliedPacket() {

    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeNbt(tag);
    }

    @Override
    public void read(FriendlyByteBuf friendlyByteBuf) {
        tag = friendlyByteBuf.readNbt();
    }


    @Override
    public void handleClient() {
        String name = tag.getString(S2CTeamPlayerDataPacket.NAME_KEY);
        UUID id = tag.getUUID(S2CTeamPlayerDataPacket.ID_KEY);
        ClientHelper.addRequestToast(ClientTeam.INSTANCE.getName(), name, id);
    }
}
