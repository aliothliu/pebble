package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.ValidationHandler;
import io.github.aliothliu.marble.infrastructure.errors.NonMatchEntityException;
import io.github.aliothliu.pebble.PebbleRegistry;
import io.github.aliothliu.pebble.domain.IdentifiedValueObject;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Entity
@Table(name = "admin_employee_transfer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployeeTransfer extends IdentifiedValueObject {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "employee_id", length = 40, updatable = false))
    })
    @NonNull
    private EmployeeId employeeId;

    @Column(name = "department_id")
    @NonNull
    private String departmentId;

    @Column(name = "transfer_date")
    @NonNull
    private LocalDateTime transferDate;

    @Column(name = "is_leader")
    private Boolean isLeader = false;

    @Transient
    @Setter(AccessLevel.PRIVATE)
    private transient Department department;

    public void validate(ValidationHandler handler) {
        if (this.departmentRepository().nonexistence(this.departmentId)) {
            handler.handleException(new NonMatchEntityException("查询失败：未找到符合条件的部门数据"));
        }
    }

    private DepartmentRepository departmentRepository() {
        return PebbleRegistry.departmentRepository();
    }

    @Nullable
    public String departmentName() {
        if (Objects.isNull(this.department)) {
            this.departmentRepository()
                    .ofId(this.departmentId)
                    .ifPresent(this::setDepartment);
        }
        if (Objects.isNull(this.department)) {
            return null;
        }

        return this.department.getName().getName();
    }
}
