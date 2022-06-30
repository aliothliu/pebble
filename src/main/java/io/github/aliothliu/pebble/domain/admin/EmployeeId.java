package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.AssertionConcern;
import io.github.aliothliu.pebble.domain.ValueObject;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class EmployeeId extends AssertionConcern implements ValueObject {

    private String id;

    public EmployeeId(String id) {
        this.assertArgumentNotNull(id, "ID不能为空");
        this.assertArgumentLength(id, 40, "ID长度不能超过40");
        this.id = id;
    }
}
