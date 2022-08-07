package com.liulin.common.constant;

public class ProductConstant {
    public enum AttrType {
        SALE(0,"sale"),
        BASE(1,"base");
        private final Integer code;
        private final String msg;

        public Integer getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }

        AttrType(Integer code, String msg) {
            this.code = code;
            this.msg = msg;
        }



    }
}
