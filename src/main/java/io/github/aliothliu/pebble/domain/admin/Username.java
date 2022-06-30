package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.AssertionConcern;
import io.github.aliothliu.pebble.domain.ValueObject;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Embeddable;
import java.util.regex.Pattern;

@Getter
@Embeddable
@FieldNameConstants
public class Username extends AssertionConcern implements ValueObject {

    private String value;

    // for hibernate
    protected Username() {

    }

    public Username(String number) {
        this.setValue(number);
    }

    public void setValue(String value) {
        this.assertArgumentNotNull(value, "用户名不能为空");
        this.assertArgumentTrue(this.isCorrect(value), "用户名格式错误，以字母开头，允许5-16字节，允许字母数字下划线");
        this.value = value;
    }

    private boolean isCorrect(String number) {
        return Pattern.matches("^[a-zA-Z][a-zA-Z0-9_]{4,15}$", number);
    }
}
