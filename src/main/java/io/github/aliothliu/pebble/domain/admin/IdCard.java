package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.AssertionConcern;
import io.github.aliothliu.pebble.infrastructure.utils.IdCardUtil;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Embeddable;

@Getter
@Embeddable
@FieldNameConstants
public class IdCard extends AssertionConcern {

    private String idCardNo;

    // for hibernate
    protected IdCard() {

    }

    public IdCard(String idCardNo) {
        this.setIdCardNo(idCardNo);
    }

    public void setIdCardNo(String idCardNo) {
        this.assertArgumentNotEmpty(idCardNo, "身份证号码不能为空");
        this.assertArgumentTrue(this.isCorrect(idCardNo), "身份证号码格式错误");

        this.idCardNo = idCardNo;
    }

    private boolean isCorrect(String idCardNo) {
        return IdCardUtil.isValidCard(idCardNo);
    }
}
