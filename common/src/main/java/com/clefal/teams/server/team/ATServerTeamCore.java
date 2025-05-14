package com.clefal.teams.server.team;

import com.google.common.base.Objects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;

import java.util.UUID;

public record ATServerTeamCore(String name, UUID identifier) {
    public static Codec<ATServerTeamCore> CODEC = RecordCodecBuilder.create(atServerTeamCoreInstance -> atServerTeamCoreInstance.group(
            Codec.STRING.fieldOf("name").forGetter(ATServerTeamCore::name),
            UUIDUtil.CODEC.fieldOf("id").forGetter(ATServerTeamCore::identifier)
    ).apply(atServerTeamCoreInstance, ATServerTeamCore::new)
    );
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ATServerTeamCore that)) return false;
        return Objects.equal(identifier, that.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(identifier);
    }
}
