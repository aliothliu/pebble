package io.github.aliothliu.pebble.domain;

import io.github.aliothliu.marble.domain.AssertionConcern;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Embeddable;
import java.util.regex.Pattern;

/**
 * Cellphone
 *
 * @author liubin
 **/
@Getter
@Embeddable
@FieldNameConstants
public class Cellphone extends AssertionConcern implements ValueObject {

    private String number;

    // for hibernate
    protected Cellphone() {

    }

    public Cellphone(String number) {
        this.setNumber(number);
    }

    public void setNumber(String number) {
        this.assertArgumentNotEmpty(number, "移动手机号不能为空");
        this.assertArgumentTrue(this.isCorrect(number), "移动手机号[" + number + "]格式错误");

        this.number = number;
    }

    private boolean isCorrect(String number) {
        return Pattern.matches("^(?:\\+?86)?1(?:3\\d{3}|5[^4\\D]\\d{2}|8\\d{3}|7(?:[0-35-9]\\d{2}|4(?:0\\d|1[0-2]|9\\d))|9[0-35-9]\\d{2}|6[2567]\\d{2}|4(?:(?:10|4[01])\\d{3}|[68]\\d{4}|[579]\\d{2}))\\d{6}$", number);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
