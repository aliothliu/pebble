package io.github.aliothliu.pebble.interfaces.department;

import io.github.aliothliu.pebble.application.admin.DepartmentApplicationService;
import io.github.aliothliu.pebble.application.admin.DepartmentQueryService;
import io.github.aliothliu.pebble.application.admin.command.ChangeDepartmentCommand;
import io.github.aliothliu.pebble.application.admin.command.InsertDepartmentCommand;
import io.github.aliothliu.pebble.application.admin.command.NewDepartmentCommand;
import io.github.aliothliu.pebble.application.admin.representation.DepartmentDetailRepresentation;
import io.github.aliothliu.pebble.application.admin.representation.DepartmentSimplifyRepresentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ops/departments")
@Tag(name = "部门管理")
@RequiredArgsConstructor
public class DepartmentResource {

    private final DepartmentQueryService queryService;

    private final DepartmentApplicationService applicationService;

    @GetMapping("/{id}")
    @Operation(description = "查询部门信息详情")
    public ResponseEntity<DepartmentDetailRepresentation> getOne(@PathVariable String id) {
        return queryService.getOne(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/forest")
    @Operation(description = "查询部门树")
    public ResponseEntity<List<DepartmentSimplifyRepresentation>> forest() {
        return ResponseEntity.ok(this.queryService.forest());
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "新建部门")
    public ResponseEntity<String> newDepartment(@RequestBody @Valid NewDepartmentCommand command) {
        return ResponseEntity.ok(this.applicationService.createDepartment(command));
    }

    @PutMapping
    @Operation(description = "修改部门")
    public ResponseEntity<String> changeDepartment(@RequestBody @Valid ChangeDepartmentCommand command) {
        return ResponseEntity.ok(this.applicationService.changeDepartment(command));
    }

    @PostMapping("/insertion")
    @Operation(description = "插入部门")
    public ResponseEntity<String> insert(@RequestBody @Valid InsertDepartmentCommand command) {
        return ResponseEntity.ok(this.applicationService.insertDepartment(command));
    }

    @PostMapping("/exchange")
    @Operation(description = "互换两个部门的顺序")
    public ResponseEntity<Void> exchange(@RequestBody @Valid ExchangeSortVM vm) {
        this.applicationService.exchange(vm.getOriginDepartmentId(), vm.getTargetDepartmentId());
        return ResponseEntity.ok().build();
    }
}
