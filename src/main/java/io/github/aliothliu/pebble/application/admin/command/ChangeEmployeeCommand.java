package io.github.aliothliu.pebble.application.admin.command;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@Schema(description = "修改用户信息参数对象")
public class ChangeEmployeeCommand {

    @NotBlank(message = "用户姓名不能为空")
    @Schema(description = "用户姓名")
    @Length(max = 255)
    private String name;

    @NotBlank(message = "移动电话号码不能为空")
    @Schema(description = "移动电话号码")
    private String cellphone;

    @Schema(description = "用户身份证号码，如无更新可不传")
    private String idCard;
}
