package com.liulin.common.exception;

public enum BizCodeEnume {
    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    INVALID_EXCEPTION(10001, "参数校验失败");

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private Integer code;
    private String msg;

    BizCodeEnume(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
