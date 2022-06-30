package io.github.aliothliu.pebble.application.admin;

import io.github.aliothliu.pebble.application.admin.command.ChangeEmployeeCommand;
import io.github.aliothliu.pebble.application.admin.command.ChangePasswordCommand;
import io.github.aliothliu.pebble.application.admin.command.NewDepartmentCommand;
import io.github.aliothliu.pebble.application.admin.command.NewEmployeeCommand;
import io.github.aliothliu.pebble.application.admin.representation.DepartmentDetailRepresentation;
import io.github.aliothliu.pebble.domain.admin.EmployeeId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("h2")
class EmployeeApplicationServiceTest {

    @Autowired
    private EmployeeApplicationService applicationService;

    @Autowired
    private EmployeeQueryService queryService;

    @Autowired
    private DepartmentApplicationService departmentApplicationService;

    @Autowired
    private DepartmentQueryService departmentQueryService;

    private String id;

    @BeforeEach
    public void setup() {
        DepartmentDetailRepresentation department = this.initialDepartmentData();

        NewEmployeeCommand command = new NewEmployeeCommand();
        command.setName("修改信息");
        command.setDepartmentId(department.getId());
        command.setCellphone("13708854348");
        command.setIdCard("110101199003072674");
        command.setPassword("9527");
        command.setUsername("ChangeInfo");

        id = this.applicationService.newEmployee(command);
    }

    @AfterEach
    public void teardown() {

    }

    @Test
    void newEmployee() {
        DepartmentDetailRepresentation department = this.initialDepartmentData();

        NewEmployeeCommand command = new NewEmployeeCommand();
        command.setName("达文西");
        command.setDepartmentId(department.getId());
        command.setCellphone("13708854343");
        command.setIdCard("110101199003076675");
        command.setPassword("admin");
        command.setUsername("liubin");

        this.applicationService.newEmployee(command);
    }

    @Test
    void changePassword() {
        DepartmentDetailRepresentation department = this.initialDepartmentData();

        NewEmployeeCommand command = new NewEmployeeCommand();
        command.setName("修改密码");
        command.setDepartmentId(department.getId());
        command.setIdCard("110101199003072674");
        command.setCellphone("13708854344");
        command.setPassword("9527");
        command.setUsername("ChangePassword");

        String id = this.applicationService.newEmployee(command);

        ChangePasswordCommand changePasswordCommand = new ChangePasswordCommand();
        changePasswordCommand.setCurrentPassword("9527");
        changePasswordCommand.setChangedPassword("0871");
        this.applicationService.changePassword(new EmployeeId(id), changePasswordCommand);
    }

    @Test
    void transfer() {

    }

    @Test
    void leave() {

    }

    @Test
    void changeEmployee() {
        ChangeEmployeeCommand changeEmployeeCommand = new ChangeEmployeeCommand();
        changeEmployeeCommand.setName("修改信息-修改");
        changeEmployeeCommand.setCellphone("13708854349");
        this.applicationService.changeEmployee(new EmployeeId(id), changeEmployeeCommand);
    }

    private DepartmentDetailRepresentation initialDepartmentData() {
        NewDepartmentCommand command = new NewDepartmentCommand();
        command.setName("开发部");
        command.setDescription("创新、数字、科技");

        String id = this.departmentApplicationService.createDepartment(command);

        return this.departmentQueryService.getOne(id).orElseThrow(IllegalAccessError::new);
    }
}