package io.github.aliothliu.pebble.interfaces.employee;

import io.github.aliothliu.marble.application.RbacUserSubject;
import io.github.aliothliu.marble.application.representation.MenuRepresentation;
import io.github.aliothliu.pebble.application.admin.EmployeeApplicationService;
import io.github.aliothliu.pebble.application.admin.EmployeeQueryService;
import io.github.aliothliu.pebble.application.admin.command.ChangeEmployeeCommand;
import io.github.aliothliu.pebble.application.admin.command.ChangePasswordCommand;
import io.github.aliothliu.pebble.application.admin.representation.EmployeeDetailRepresentation;
import io.github.aliothliu.pebble.domain.admin.EmployeeId;
import io.github.aliothliu.pebble.infrastructure.security.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/ops/current")
@Tag(name = "个人中心")
@RequiredArgsConstructor
public class MineResource {

    private final EmployeeQueryService queryService;

    private final EmployeeApplicationService applicationService;

    @GetMapping
    @Operation(description = "查询当前登录用户信息")
    public ResponseEntity<EmployeeDetailRepresentation> current() {
        return this.queryService.getOne(new EmployeeId(SecurityUtil.getCurrentUserLogin())).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/menus")
    @Operation(description = "查询当前登录用户菜单")
    public ResponseEntity<List<MenuRepresentation>> menus() {
        return ResponseEntity.ok(new RbacUserSubject(SecurityUtil.getCurrentUserLogin()).loadMenuForest());
    }

    @PostMapping("/password")
    @Operation(description = "修改当前用户登录密码")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordCommand command) {
        this.applicationService.changePassword(new EmployeeId(SecurityUtil.getCurrentUserLogin()), command);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    @Operation(description = "修改当前用户信息")
    public ResponseEntity<Void> update(@RequestBody @Valid ChangeEmployeeCommand command) {
        this.applicationService.changeEmployee(new EmployeeId(SecurityUtil.getCurrentUserLogin()), command);
        return ResponseEntity.ok().build();
    }

}
