package io.github.aliothliu.pebble.domain;

import io.github.aliothliu.marble.domain.ValidationHandler;

import javax.persistence.MappedSuperclass;

/**
 * An entity, as explained in the DDD book.
 */
@MappedSuperclass
public abstract class Entity extends AuditableIdentifiedEntity {
    private static final long serialVersionUID = 1L;

    protected Entity() {
        super();
    }

    public abstract void validate(ValidationHandler handler);
}
