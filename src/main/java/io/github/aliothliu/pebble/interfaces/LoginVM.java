package io.github.aliothliu.pebble.interfaces;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * View Model object for storing a user's credentials.
 */
@Schema(description = "登录请求参数对象")
@Data
@Slf4j
public class LoginVM {

    // 账号
    @NotBlank
    @Length(min = 5, max = 15)
    @Schema(description = "用户账户", required = true)
    private String username;
    // 密码
    @NotBlank
    @Length
    @Schema(description = "用户密码", required = true)
    private String password;

    @Schema(description = "记住我")
    private boolean rememberMe;
}
