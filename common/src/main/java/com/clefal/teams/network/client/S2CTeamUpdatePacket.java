package com.clefal.teams.network.client;

import com.clefal.nirvana_lib.network.newtoolchain.S2CModPacket;
import com.clefal.teams.utils.ClientHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class S2CTeamUpdatePacket implements S2CModPacket<S2CTeamUpdatePacket> {

    private static final String TEAM_KEY = "teamName";
    private static final String PLAYER_KEY = "playerName";
    private static final String ACTION_KEY = "action";
    private static final String LOCAL_KEY = "local";
    CompoundTag tag = new CompoundTag();

    public S2CTeamUpdatePacket(String team, String player, Action action, boolean isLocal) {
        tag.putString(TEAM_KEY, team);
        tag.putString(PLAYER_KEY, player);
        tag.putString(ACTION_KEY, action.name());
        tag.putBoolean(LOCAL_KEY, isLocal);
    }

    public S2CTeamUpdatePacket() {
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
    public Class<S2CTeamUpdatePacket> getSelfClass() {
        return S2CTeamUpdatePacket.class;
    }

    @Override
    public void handleClient() {
        String team = tag.getString(TEAM_KEY);
        String player = tag.getString(PLAYER_KEY);
        S2CTeamUpdatePacket.Action action = S2CTeamUpdatePacket.Action.valueOf(tag.getString(ACTION_KEY));
        boolean isLocal = tag.getBoolean(LOCAL_KEY);
        ClientHelper.addJoinOrLeaveToast(action, team, player, isLocal);
    }


    public enum Action {
        JOINED,
        LEFT
    }
}
