package io.github.aliothliu.pebble.application.admin;

import io.github.aliothliu.pebble.application.admin.command.InsertDepartmentCommand;
import io.github.aliothliu.pebble.application.admin.command.NewDepartmentCommand;
import io.github.aliothliu.pebble.application.admin.representation.DepartmentDetailRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("h2")
class DepartmentApplicationServiceTest {

    @Autowired
    private DepartmentApplicationService applicationService;

    @Autowired
    private DepartmentQueryService queryService;

    @Test
    void createDepartment() {

        NewDepartmentCommand command = new NewDepartmentCommand();
        command.setName("开发部");
        command.setDescription("创新、数字、科技");

        String id = applicationService.createDepartment(command);

        Optional<DepartmentDetailRepresentation> opt = queryService.getOne(id);
        assertTrue(opt.isPresent());

        DepartmentDetailRepresentation department = opt.get();

        assertEquals("开发部", department.getName());
        assertEquals("创新、数字、科技", department.getDescription());
        assertNotNull(department.getId());
        assertNull(department.getParentId());
    }

    @Test
    void changeDepartment() {
    }

    @Test
    void insertDepartment() {
        String pid = null;
        for (int i = 1; i <= 10; i++) {
            NewDepartmentCommand command = new NewDepartmentCommand();
            command.setName("root-department-" + i);
            pid = this.applicationService.createDepartment(command);

            for (int j = 1; j <= 10; j++) {
                NewDepartmentCommand subCommand = new NewDepartmentCommand();
                subCommand.setName("sub-department-" + j);
                subCommand.setParentId(pid);

                this.applicationService.createDepartment(subCommand);
            }
        }

        InsertDepartmentCommand command = new InsertDepartmentCommand();
        command.setBrotherId(pid);
        command.setForward(true);
        command.setName("插入测试");

        this.applicationService.insertDepartment(command);
    }

    @Test
    void exchange() {
    }
}