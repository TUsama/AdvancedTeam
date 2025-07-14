package com.clefal.teams.server;

import com.google.common.base.Objects;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class Application extends ExpirableObject{
    UUID applicant;


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
