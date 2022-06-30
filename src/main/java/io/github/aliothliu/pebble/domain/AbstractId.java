package io.github.aliothliu.pebble.domain;

import io.github.aliothliu.marble.domain.AssertionConcern;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@MappedSuperclass
@FieldNameConstants
public abstract class AbstractId extends AssertionConcern implements Identity, Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    protected AbstractId(String anId) {
        this();

        this.setId(anId);
    }

    protected AbstractId() {
        super();
    }

    @Override
    public String getId() {
        return this.id;
    }

    private void setId(String anId) {
        this.assertArgumentNotEmpty(anId, "ID不能为空");
        this.assertArgumentLength(anId, 40, "ID长度不能超过40字符");

        this.validate(anId);

        this.id = anId;
    }

    public void validate(String anId) {
        // implemented by subclasses for validation.
        // throws a runtime exception if invalid.
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractId)) {
            return false;
        }
        AbstractId that = (AbstractId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
