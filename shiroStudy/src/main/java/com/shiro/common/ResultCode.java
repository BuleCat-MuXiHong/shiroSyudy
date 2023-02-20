package com.shiro.common;

public enum ResultCode {
    SUCCESS(200, "访问成功"),
    SYSTEM_FAILURE(500, "系统异常"),
    ;

    private final int code;
    private final String description;

    ResultCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
