package io.github.aliothliu.pebble.application.admin.representation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "部门层次视图对象")
@AllArgsConstructor
@Builder
public class DepartmentSimplifyRepresentation {

    @Schema(description = "部门ID")
    private String id;

    @Schema(description = "部门名称")
    private String name;

    @Schema(description = "部门描述")
    private String description;

    @Schema(description = "子部门列表")
    private List<DepartmentSimplifyRepresentation> children;
}
