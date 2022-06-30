package io.github.aliothliu.pebble.application.admin.representation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Schema(description = "部门视图对象")
@AllArgsConstructor
@Builder
public class DepartmentDetailRepresentation {

    @Schema(description = "部门ID")
    private String id;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "父级部门ID")
    private String parentId;

    @Schema(description = "父级部门名称")
    private String parentName;

    @Schema(description = "部门描述")
    private String description;

}