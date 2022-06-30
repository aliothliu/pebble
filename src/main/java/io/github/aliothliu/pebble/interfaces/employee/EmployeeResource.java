package io.github.aliothliu.pebble.interfaces.employee;

import io.github.aliothliu.marble.Subject;
import io.github.aliothliu.marble.application.RbacUserSubject;
import io.github.aliothliu.marble.domain.role.RoleCode;
import io.github.aliothliu.pebble.application.admin.EmployeeApplicationService;
import io.github.aliothliu.pebble.application.admin.EmployeeQueryService;
import io.github.aliothliu.pebble.application.admin.command.ChangeEmployeeCommand;
import io.github.aliothliu.pebble.application.admin.command.NewEmployeeCommand;
import io.github.aliothliu.pebble.application.admin.query.EmployeeQuery;
import io.github.aliothliu.pebble.application.admin.representation.EmployeeDetailRepresentation;
import io.github.aliothliu.pebble.application.admin.representation.EmployeeRepresentation;
import io.github.aliothliu.pebble.domain.admin.EmployeeId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ops/employees")
@Tag(name = "用户管理")
@RequiredArgsConstructor
public class EmployeeResource {

    private final EmployeeQueryService queryService;

    private final EmployeeApplicationService applicationService;

    @GetMapping("/{id}")
    @Operation(description = "查询用户信息")
    public ResponseEntity<EmployeeDetailRepresentation> getOne(@PathVariable String id) {
        return this.queryService.getOne(new EmployeeId(id)).map(ResponseEntity::ok).orElse(ResponseEntity.noContent().build());
    }

    @GetMapping
    @Operation(description = "查询用户列表")
    public ResponseEntity<Page<EmployeeRepresentation>> list(EmployeeQuery query, Pageable pageable) {
        return ResponseEntity.ok(this.queryService.paging(query, pageable));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "新建用户信息")
    public ResponseEntity<String> create(@RequestBody @Valid NewEmployeeCommand command) {
        return ResponseEntity.ok(this.applicationService.newEmployee(command));
    }

    @PutMapping("/{id}")
    @Operation(description = "修改用户信息")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody @Valid ChangeEmployeeCommand command) {
        this.applicationService.changeEmployee(new EmployeeId(id), command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reset-password")
    @Operation(description = "重置用户密码")
    public ResponseEntity<Void> resetPassword(@PathVariable String id) {
        this.applicationService.resetPassword(new EmployeeId(id));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/leaved")
    @Operation(description = "设置用户工作状态为离职")
    public ResponseEntity<Void> leave(@PathVariable String id) {
        this.applicationService.leave(new EmployeeId(id));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/grant")
    @Operation(description = "配置角色用户关联")
    public ResponseEntity<Object> assignUserToRole(@PathVariable String id, @RequestBody GrantRoleToUserVM vm) {
        return this.queryService
                .getOne(new EmployeeId(id))
                .map(e -> {
                    Subject subject = new RbacUserSubject(e.getId());
                    subject.revokeAll();
                    subject.grantAll(vm.getRoles().stream().map(RoleCode::new).collect(Collectors.toList()));

                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
