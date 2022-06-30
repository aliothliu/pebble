package io.github.aliothliu.pebble.application.admin.representation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "用户视图对象")
public class EmployeeRepresentation {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "用户账户")
    private String username;

    @Schema(description = "用户姓名")
    private String name;

    @Schema(description = "身份证号，脱敏")
    private String idCardNo;

    @Schema(description = "出生日期")
    private LocalDate birthDay;

    @Schema(description = "性别")
    private String gender;

    @Schema(description = "性别描述")
    private String genderDesc;

    @Schema(description = "信息系统监理师证书号码")
    private String supervisorCertNo;

    @Schema(description = "移动电话号码")
    private String cellphone;

    @Schema(description = "部门ID")
    private String departmentId;

    @Schema(description = "部门名称")
    private String departmentName;

    @Schema(description = "工作状态")
    private String workStatus;

    @Schema(description = "工作状态描述")
    private String workStatusDesc;
}
