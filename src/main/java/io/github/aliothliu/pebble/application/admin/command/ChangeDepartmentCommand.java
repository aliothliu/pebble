package io.github.aliothliu.pebble.application.admin.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "修改部门参数对象")
public class ChangeDepartmentCommand {

    @NotBlank(message = "部门ID不能为空")
    @Schema(description = "部门ID")
    private String id;

    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 255, message = "部门名称长度不能超过{max}")
    private String name;

    @Schema(description = "父级部门ID")
    private String parentId;

    @Schema(description = "部门描述")
    @Length(max = 255, message = "部门描述长度不能超过{max}")
    private String description;
}
