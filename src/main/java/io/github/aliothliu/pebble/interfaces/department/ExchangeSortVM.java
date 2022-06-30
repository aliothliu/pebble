package io.github.aliothliu.pebble.interfaces.department;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "部门互换顺序参数对象")
public class ExchangeSortVM {
    @NotBlank(message = "部门ID不能为空")
    @Schema(description = "部门ID")
    private String originDepartmentId;
    @NotBlank(message = "部门ID不能为空")
    @Schema(description = "部门ID")
    private String targetDepartmentId;
}
