package io.github.aliothliu.pebble.domain.admin;

public enum Gender {

    Male("男"),
    Female("女");

    private final String desc;

    Gender(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
