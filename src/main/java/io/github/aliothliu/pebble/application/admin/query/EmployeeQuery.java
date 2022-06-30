package io.github.aliothliu.pebble.application.admin.query;

import io.github.aliothliu.pebble.domain.admin.WorkStatus;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;

@Data
public class EmployeeQuery {

    @Parameter(description = "工作状态")
    WorkStatus.Status status;
    @Parameter(description = "用户姓名，模糊查询")
    private String name;
    @Parameter(description = "电话号码，模糊查询")
    private String cellphone;
    @Parameter(description = "用户账户，模糊查询")
    private String username;
    @Parameter(description = "部门ID")
    private String departmentId;
}
