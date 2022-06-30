package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.AssertionConcern;
import io.github.aliothliu.pebble.domain.ValueObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * WorkStatus
 *
 * @author liubin
 **/
@EqualsAndHashCode(callSuper = false)
@Getter
@FieldNameConstants
@Embeddable
public class WorkStatus extends AssertionConcern implements ValueObject {

    private final Boolean updatable = true;

    @Enumerated(EnumType.STRING)
    private Status status;

    // for hibernate
    protected WorkStatus() {
    }

    private WorkStatus(Status status) {
        this.status = status;
    }

    public static WorkStatus newEmploy() {
        return new WorkStatus(Status.Employed);
    }

    public void employ() {
        this.status = Status.Employed;
    }

    public void leave() {
        this.status = Status.Leaved;
    }

    public boolean isEmployed() {
        return Status.Employed.equals(this.status);
    }

    public enum Status {
        Employed("在职"),
        Leaved("离职");

        private final String desc;

        Status(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
    }
}
