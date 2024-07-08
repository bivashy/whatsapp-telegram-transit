package io.github.bivashy.wttj.database.domain;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import java.time.Instant;

@MappedSuperclass
public class BaseDomain extends Model {

    @Id
    Long id;
    @Version
    long version;
    @WhenCreated
    Instant whenCreated;
    @WhenModified
    Instant whenModified;

    public Long getId() {
        return id;
    }

    public long getVersion() {
        return version;
    }

    public Instant getWhenCreated() {
        return whenCreated;
    }

    public Instant getWhenModified() {
        return whenModified;
    }

}
