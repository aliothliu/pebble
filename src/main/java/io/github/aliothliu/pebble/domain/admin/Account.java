package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.ValidationHandler;
import io.github.aliothliu.pebble.PebbleRegistry;
import io.github.aliothliu.pebble.domain.AuditableEntity;
import io.github.aliothliu.pebble.infrastructure.errors.NonUniquenessException;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Getter
@Entity
@Table(name = "admin_employee_auth")
@FieldNameConstants
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class Account extends AuditableEntity {

    // 账户
    @Id
    @NonNull
    private String identifier;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "id", column = @Column(name = "employee_id", length = 40, updatable = false))
    })
    @NonNull
    private EmployeeId employeeId;

    // 密码
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "password", column = @Column(name = "credential", length = 128, updatable = false))
    })
    @Column(name = "credential")
    @NonNull
    private Password credential;

    // 验证提供方
    @Column(name = "auth_provider", updatable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private AccountProvider provider;

    // 是否启用
    @Column(name = "enabled")
    private Boolean enabled = Boolean.TRUE;

    // QQ / 微信同一主体下 Unionid 相同
    @Column(name = "union_id")
    private String unionid;

    @Transient
    @Setter(AccessLevel.PRIVATE)
    private transient Employee employee;

    public void validate(ValidationHandler handler) {
        if (this.repository().exists(this.identifier)) {
            handler.handleException(new NonUniquenessException("该登录标识已被绑定[" + this.identifier + "]"));
        }
    }

    private AccountRepository repository() {
        return PebbleRegistry.accountRepository();
    }

    public Account newAccount(String newIdentifier) {
        this.assertArgumentNotEmpty(newIdentifier, "登录账户不能为空");
        return new Account(newIdentifier, this.employeeId, this.credential, this.provider);
    }

    public void changePassword(String currentPassword, String changedPassword) {
        this.assertArgumentNotEmpty(currentPassword, "登录密码不能为空");
        this.assertArgumentNotEmpty(changedPassword, "登录密码不能为空");

        this.assertArgumentTrue(this.credential.matches(currentPassword), "原密码不正确");

        this.credential = new Password(changedPassword);
    }

    public void resetPassword(String newPassword) {
        this.credential = new Password(newPassword);
    }

    public Account loadEmployee() {
        if (Objects.isNull(employee)) {
            this.employeeRepository().ofId(this.employeeId).ifPresent(this::setEmployee);
        }
        return this;
    }

    public void disabled() {
        this.enabled = false;
    }

    public void enable() {
        this.enabled = true;
    }

    private EmployeeRepository employeeRepository() {
        return PebbleRegistry.employeeRepository();
    }

    private PasswordService passwordService() {
        return PebbleRegistry.passwordService();
    }
}
