package io.github.aliothliu.pebble.application.admin.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "新建用户请求参数对象")
public class NewEmployeeCommand {

    @NotBlank(message = "用户姓名不能为空")
    @Length(max = 255)
    @Schema(description = "用户姓名")
    private String name;

    @NotBlank(message = "用户部门不能为空")
    @Schema(description = "用户部门")
    private String departmentId;

    @NotBlank(message = "用户身份证号不能为空")
    @Schema(description = "用户身份证号")
    private String idCard;

    @NotBlank(message = "用户联系电话不能为空")
    @Schema(description = "联系电话")
    private String cellphone;

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String username;

    @NotBlank(message = "用户密码不能为空 ")
    @Schema(description = "用户密码")
    @Length(max = 32, message = "密码长度不能超过{max}")
    private String password;
}
