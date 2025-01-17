package com.clefal.teams.network.client;

import com.clefal.teams.client.core.ClientTeam;
import com.clefal.teams.client.gui.toast.ToastRequested;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.UUID;


public class S2CTeamRequestedPacket implements S2CModPacket {


    CompoundTag tag = new CompoundTag();
    public S2CTeamRequestedPacket(String name, UUID id) {
        tag.putString(S2CTeamPlayerDataPacket.NAME_KEY, name);
        tag.putUUID(S2CTeamPlayerDataPacket.ID_KEY, id);
    }

    public S2CTeamRequestedPacket(FriendlyByteBuf byteBuf) {
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
        Minecraft.getInstance().getToasts().addToast(new ToastRequested(ClientTeam.INSTANCE.getName(), name, id));
    }
}
