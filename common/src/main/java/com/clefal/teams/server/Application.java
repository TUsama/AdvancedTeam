package com.clefal.teams.server;

import com.google.common.base.Objects;

import java.util.UUID;

public class Application extends ExpirableObject{
    UUID applicant;

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
}
