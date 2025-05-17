package com.clefal.teams.server.team;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class ATServerTeamConfig {
    public static Codec<ATServerTeamConfig> CODEC = RecordCodecBuilder.create(atServerTeamConfigInstance -> atServerTeamConfigInstance.group(
                    Codec.BOOL.fieldOf("pu").forGetter(x -> x.isPublic),
                    Codec.BOOL.fieldOf("aei").forGetter(x -> x.allowEveryoneInvite)

            ).apply(atServerTeamConfigInstance, ATServerTeamConfig::new)
    );
    public boolean isPublic;
    public boolean allowEveryoneInvite;


    public ATServerTeamConfig(boolean isPublic, boolean allowEveryoneInvite) {
        this.isPublic = isPublic;
        this.allowEveryoneInvite = allowEveryoneInvite;

    }

}
