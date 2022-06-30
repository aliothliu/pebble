package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.ValidationHandler;
import io.github.aliothliu.marble.infrastructure.errors.NonMatchEntityException;
import io.github.aliothliu.pebble.PebbleRegistry;
import io.github.aliothliu.pebble.domain.Cellphone;
import io.github.aliothliu.pebble.domain.ConcurrencySafeEntity;
import io.github.aliothliu.pebble.domain.PhoneticizeName;
import io.github.aliothliu.pebble.infrastructure.utils.IdCardUtil;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Getter
@Entity
@Table(name = "admin_employee")
@FieldNameConstants
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee extends ConcurrencySafeEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "employee_id", length = 40, unique = true, updatable = false))
    })
    @NonNull
    private EmployeeId employeeId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "value", column = @Column(name = "username", length = 40, unique = true, updatable = false))
    })
    @NonNull
    private Username username;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "name")),
            @AttributeOverride(name = "phoneticize", column = @Column(name = "phoneticize"))
    })
    @Setter(value = AccessLevel.PRIVATE)
    @NonNull
    private PhoneticizeName name;

    @Column(name = "gender", length = 8)
    private Gender gender;

    @Column(name = "birthday", length = 8)
    private LocalDate birthday;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "idCardNo", column = @Column(name = "id_card_no", length = 18))
    })
    @NonNull
    private IdCard idCard;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "number", column = @Column(name = "mobile_phone"))
    })
    @Setter(value = AccessLevel.PRIVATE)
    @NonNull
    private Cellphone cellphone;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "status", column = @Column(name = "work_status")),
            @AttributeOverride(name = "updatable", column = @Column(name = "work_status_updatable"))
    })
    @NonNull
    private WorkStatus workStatus;

    /***********导航属性**************/
    @Setter(AccessLevel.PUBLIC)
    @Transient
    private transient Department department;

    public Employee(@NonNull EmployeeId employeeId,
                    @NonNull Username username,
                    @NonNull PhoneticizeName name,
                    @NonNull IdCard idCard,
                    @NonNull Cellphone cellphone,
                    @NonNull WorkStatus workStatus) {
        this.employeeId = employeeId;
        this.username = username;
        this.name = name;
        this.idCard = idCard;
        this.gender = this.genderFromIdCardNo(idCard.getIdCardNo());
        this.birthday = IdCardUtil.getBirthDate(idCard.getIdCardNo());
        this.cellphone = cellphone;
        this.workStatus = workStatus;
    }

    @Override
    public void validate(ValidationHandler handler) {

    }

    public EmployeeTransfer joinIn(String departmentId) {
        return this.transfer(departmentId);
    }

    public List<Account> newAccounts(String password) {
        return Arrays.asList(
                new Account(this.username.getValue(), employeeId, new Password(password), AccountProvider.Local),
                new Account(this.cellphone.getNumber(), employeeId, new Password(password), AccountProvider.Local));
    }

    public Employee leave() {
        this.assertArgumentTrue(this.workStatus.getUpdatable(), "该用户不允许离职");
        this.workStatus.leave();
        return this;
    }

    public boolean isEmployed() {
        return this.workStatus.isEmployed();
    }

    public EmployeeTransfer transfer(String newDepartmentId) {
        this.transferRepository().latest(this.employeeId).ifPresent(t -> {
            if (t.getDepartmentId().equals(newDepartmentId)) {
                throw new IllegalArgumentException("操作失败：员工部门未发生变动");
            }
        });
        return new EmployeeTransfer(this.employeeId, newDepartmentId, LocalDateTime.now());
    }

    private EmployeeTransferRepository transferRepository() {
        return PebbleRegistry.transferRepository();
    }

    public void changeName(PhoneticizeName name) {
        this.assertArgumentNotNull(name, "用户姓名不能为空");
        this.setName(name);
    }

    public void changePhone(Cellphone cellphone) {
        this.assertArgumentNotNull(cellphone, "电话号码不能为空");
        if (this.cellphone.equals(cellphone)) {
            return;
        }
        this.setCellphone(cellphone);
    }

    public void changeIdCard(String idCardNo) {
        if (Objects.isNull(idCardNo)) {
            return;
        }
        if (Objects.nonNull(this.getIdCard()) && idCardNo.equals(this.getIdCard().getIdCardNo())) {
            return;
        }

        this.idCard = new IdCard(idCardNo);
        this.gender = this.genderFromIdCardNo(this.idCard.getIdCardNo());
        this.birthday = IdCardUtil.getBirthDate(this.idCard.getIdCardNo());
    }

    private Gender genderFromIdCardNo(String idCardNo) {
        return IdCardUtil.getGenderByIdCard(idCardNo) == 1 ? Gender.Male : Gender.Female;
    }

    private AccountRepository accountRepository() {
        return PebbleRegistry.accountRepository();
    }

    public Optional<Department> department() {
        if (Objects.nonNull(this.department)) {
            return Optional.of(this.department);
        }
        String departmentId = PebbleRegistry.transferRepository()
                .latest(this.employeeId)
                .map(EmployeeTransfer::getDepartmentId)
                .orElseThrow(() -> new NonMatchEntityException("操作失败: 员工未关联任何部门信息"));
        this.loadDepartment(departmentId);

        return Optional.of(this.department);
    }

    private void loadDepartment(String departmentId) {
        PebbleRegistry.departmentRepository().ofId(departmentId).ifPresent(this::setDepartment);
    }
}
