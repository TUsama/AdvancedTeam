package com.clefal.teams.server.team;

import com.clefal.nirvana_lib.utils.NetworkUtils;
import com.clefal.teams.network.client.config.S2CTeamConfigBooleanPacket;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class ATServerTeamConfig {
    public static Codec<ATServerTeamConfig> CODEC = RecordCodecBuilder.create(atServerTeamConfigInstance -> atServerTeamConfigInstance.group(
            Codec.BOOL.fieldOf("pu").forGetter(x -> x.isPublic),
            Codec.BOOL.fieldOf("aei").forGetter(x -> x.allowEveryoneInvite),
            Codec.BOOL.fieldOf("evc").forGetter(x -> x.enableVanillaTeamCompat),
            Codec.BOOL.fieldOf("sa").forGetter(x -> x.syncAdvancement)
            ).apply(atServerTeamConfigInstance, ATServerTeamConfig::new)
    );
    public boolean isPublic;
    public boolean allowEveryoneInvite;
    public boolean enableVanillaTeamCompat;
    public boolean syncAdvancement;

    public ATServerTeamConfig(boolean isPublic, boolean allowEveryoneInvite, boolean enableVanillaTeamCompat, boolean syncAdvancement) {
        this.isPublic = isPublic;
        this.allowEveryoneInvite = allowEveryoneInvite;
        this.enableVanillaTeamCompat = enableVanillaTeamCompat;
        this.syncAdvancement = syncAdvancement;
    }

}
