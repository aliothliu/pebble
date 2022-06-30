package io.github.aliothliu.pebble.application.admin.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "修改密码参数对象")
public class ChangePasswordCommand {

    @Schema(description = "当前密码")
    @NotBlank(message = "当前密码不能为空")
    @Length(max = 32, message = "密码长度不能超过{max}")
    private String currentPassword;

    @Schema(description = "修改的密码")
    @NotBlank(message = "修改的密码不能为空")
    @Length(max = 32, message = "密码长度不能超过{max}")
    private String changedPassword;
}
