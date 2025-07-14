package com.clefal.teams.server;

import com.google.common.base.Objects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class Application extends ExpirableObject{
    UUID applicant;

    public static Codec<Application> CODEC = RecordCodecBuilder.create(applicationInstance -> applicationInstance.group(
            UUIDUtil.CODEC.fieldOf("applicant").forGetter(x -> x.applicant),
            Codec.INT.fieldOf("lifetime").forGetter(x -> x.lifetime)
    ).apply(applicationInstance, Application::new)
    );

    public Application(UUID applicant, int lifeTime) {
        lifetime = lifeTime;
        this.applicant = applicant;
    }
    public Application(UUID applicant) {
        this.applicant = applicant;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Application that)) return false;
        return Objects.equal(applicant, that.applicant);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(applicant);
    }

    public CompoundTag toNBT(){
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putUUID("uuid", applicant);
        compoundTag.putInt("life", lifetime);
        return compoundTag;
    }

    public static Application fromNBT(CompoundTag compoundTag){
        UUID uuid = compoundTag.getUUID("uuid");
        int life = compoundTag.getInt("life");
        Application application = new Application(uuid);
        application.lifetime = life;
        return application;
    }
}
