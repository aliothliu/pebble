package io.github.aliothliu.pebble.application.admin.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "用户调动请求参数对象")
public class TransferEmployeeCommand {

    @NotBlank(message = "用户部门不能为空")
    @Schema(description = "用户部门")
    private String departmentId;
}
