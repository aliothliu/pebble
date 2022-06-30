package io.github.aliothliu.pebble.domain;

import io.github.aliothliu.marble.domain.AssertionConcern;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@FieldNameConstants
@MappedSuperclass
public abstract class IdentifiedValueObject extends AssertionConcern implements ValueObject, Identity {

    @Id
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "uuid")
    private String id;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
