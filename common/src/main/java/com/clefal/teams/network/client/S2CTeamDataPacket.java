package com.clefal.teams.network.client;

import com.clefal.teams.client.core.ClientTeamData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamDataPacket implements S2CModPacket {

    private static final String TEAM_KEY = "teamName";
    private static final String TYPE_KEY = "type";

    public enum Type {
        ADD,
        REMOVE,
        ONLINE,
        OFFLINE,
        CLEAR
    }

    CompoundTag tag = new CompoundTag();

    public S2CTeamDataPacket(Type type, String... teams) {
        ListTag nbtList = new ListTag();
        for (var team : teams) {
            nbtList.add(StringTag.valueOf(team));
        }
        tag.put(TEAM_KEY, nbtList);
        tag.putString(TYPE_KEY, type.name());
    }

    public S2CTeamDataPacket(FriendlyByteBuf byteBuf) {
        tag = byteBuf.readNbt();
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeNbt(tag);
    }

    @Override
    public void handleClient() {
        Type type = Type.valueOf(tag.getString(TYPE_KEY));
        ListTag nbtList = tag.getList(TEAM_KEY, Tag.TAG_STRING);
        for (var elem : nbtList) {
            String team = elem.getAsString();
            switch (type) {
                case ADD -> ClientTeamData.INSTANCE.addTeam(team);
                case REMOVE -> ClientTeamData.INSTANCE.removeTeam(team);
                case ONLINE -> ClientTeamData.INSTANCE.teamOnline(team);
                case OFFLINE -> ClientTeamData.INSTANCE.teamOffline(team);
                case CLEAR -> ClientTeamData.INSTANCE.clear();
            }
        }
    }
}
