package io.github.aliothliu.pebble.application.admin;

import io.github.aliothliu.marble.domain.FailFastValidationHandler;
import io.github.aliothliu.marble.domain.Sort;
import io.github.aliothliu.pebble.application.admin.command.ChangeDepartmentCommand;
import io.github.aliothliu.pebble.application.admin.command.InsertDepartmentCommand;
import io.github.aliothliu.pebble.application.admin.command.NewDepartmentCommand;
import io.github.aliothliu.pebble.domain.PhoneticizeName;
import io.github.aliothliu.pebble.domain.admin.Department;
import io.github.aliothliu.pebble.domain.admin.DepartmentRepository;
import io.github.aliothliu.pebble.domain.admin.DepartmentSortService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;

@RequiredArgsConstructor
@Service
@Validated
public class DepartmentApplicationService {

    private static final String MSG_NOT_FOUND = "操作失败，未查询到符合条件的部门数据";

    private final DepartmentSortService sortService;

    private final DepartmentRepository departmentRepository;

    @Transactional
    public String createDepartment(@Valid NewDepartmentCommand command) {
        Department department = new Department(
                new PhoneticizeName(command.getName()),
                sortService.last(command.getParentId()),
                command.getParentId(),
                command.getDescription()
        );
        department.validate(new FailFastValidationHandler());
        this.departmentRepository.save(department).loadParent();

        return department.getId();
    }

    public String changeDepartment(@Valid ChangeDepartmentCommand command) {
        Department department = this.departmentRepository.ofId(command.getId()).orElseThrow(() -> new IllegalArgumentException(MSG_NOT_FOUND));
        department.changeName(new PhoneticizeName(command.getName()));
        department.changeParent(command.getParentId());

        department.setDescription(command.getDescription());
        department.validate(new FailFastValidationHandler());
        this.departmentRepository.save(department);
        return department.getId();
    }

    public String insertDepartment(@Valid InsertDepartmentCommand command) {
        Department brother = this.departmentRepository
                .ofId(command.getBrotherId())
                .orElseThrow(() -> new IllegalArgumentException(MSG_NOT_FOUND));
        Department department = new Department(
                new PhoneticizeName(command.getName()),
                sortService.insert(command.getBrotherId()),
                brother.getParentId(),
                command.getDescription()
        );
        department.validate(new FailFastValidationHandler());
        this.departmentRepository.save(department).loadParent();
        return department.getId();
    }

    public void exchange(@NotBlank String originDepartmentId, @NotBlank String targetDepartmentId) {
        Department originDepartment = this.departmentRepository.ofId(originDepartmentId).orElseThrow(() -> new IllegalArgumentException(MSG_NOT_FOUND));
        Department targetDepartment = this.departmentRepository.ofId(targetDepartmentId).orElseThrow(() -> new IllegalArgumentException(MSG_NOT_FOUND));
        Sort exchangeSort = originDepartment.getSort();
        originDepartment.setSort(targetDepartment.getSort());
        targetDepartment.setSort(exchangeSort);
        this.departmentRepository.saveAll(Arrays.asList(originDepartment, targetDepartment));
    }
}
