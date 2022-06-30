package io.github.aliothliu.pebble.interfaces.employee;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class GrantRoleToUserVM {

    @NotEmpty
    private List<String> roles;
}
