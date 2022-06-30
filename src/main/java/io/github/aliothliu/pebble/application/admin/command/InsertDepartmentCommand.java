package io.github.aliothliu.pebble.application.admin.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "插入部门参数对象")
public class InsertDepartmentCommand {

    @NotBlank(message = "部门名称不能为空")
    @Schema(description = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 255, message = "部门名称长度不能超过{max}")
    private String name;

    @Schema(description = "兄弟节点，用于标记插入或者追加位置")
    private String brotherId;

    @Schema(description = "true 表示插入，false 表示追加，插入表示在brother部门前插入此部门，追加表示在brother部门后插入此部门")
    private boolean forward;

    @Schema(description = "部门描述")
    @Length(max = 255, message = "部门描述长度不能超过{max}")
    private String description;
}
