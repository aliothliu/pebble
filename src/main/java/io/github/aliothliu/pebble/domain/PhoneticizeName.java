package io.github.aliothliu.pebble.domain;

import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import javax.persistence.Embeddable;
import java.util.Objects;

@Slf4j
@Getter
@Embeddable
@FieldNameConstants
public class PhoneticizeName implements ValueObject {

    private String name;

    private String phoneticize;

    // for hibernate
    protected PhoneticizeName() {

    }

    public PhoneticizeName(String name) {
        Assert.notNull(name, "Name should not be null");
        try {
            this.phoneticize = PinyinHelper.getShortPinyin(name);
        } catch (PinyinException e) {
            log.error("Failed to convert name to Chinese Phoneticize");
        }
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhoneticizeName)) {
            return false;
        }
        PhoneticizeName that = (PhoneticizeName) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(phoneticize, that.phoneticize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneticize);
    }
}
