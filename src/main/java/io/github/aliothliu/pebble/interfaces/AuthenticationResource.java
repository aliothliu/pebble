package io.github.aliothliu.pebble.interfaces;

import io.github.aliothliu.pebble.infrastructure.security.AuthenticationService;
import io.github.aliothliu.pebble.infrastructure.security.JWTConfigurer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ops")
@Tag(name = "系统登录登出")
public class AuthenticationResource {

    private final AuthenticationService authenticationService;

    public AuthenticationResource(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/authenticate")
    @Operation(description = "系统登录")
    public ResponseEntity<Map<String, Object>> authorize(@RequestBody @Valid @Parameter(name = "登录请求参数") LoginVM vm) {
        String jwt = this.authenticationService.authenticate(vm.getUsername(), vm.getPassword(), vm.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
        Map<String, Object> responseMapper = new HashMap<>();
        responseMapper.put("token", jwt);
        return new ResponseEntity<>(responseMapper, httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Operation(description = "系统登出")
    public ResponseEntity<String> logout() {
        this.authenticationService.logout();
        return ResponseEntity.ok().build();
    }
}
