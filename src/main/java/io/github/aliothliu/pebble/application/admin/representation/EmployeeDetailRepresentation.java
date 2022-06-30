package io.github.aliothliu.pebble.application.admin.representation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户详细信息对象")
public class EmployeeDetailRepresentation extends EmployeeRepresentation {

    @Schema(description = "调动记录")
    private List<Transfer> transfers = new ArrayList<>();

    private List<String> roles = new ArrayList<>();

    @Data
    @Schema(description = "用户岗位变动记录")
    public static class Transfer {

        @Schema(description = "部门ID")
        private String departmentId;

        @Schema(description = "部门名称")
        private String departmentName;

        @Schema(description = "调动日期")
        private LocalDateTime transferDate;
    }
}
