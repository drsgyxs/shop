package com.shop.enums;

public enum OrderState {
    CLOSED(-1, "交易关闭"),
    NOPAY(0, "待支付"),
    NOSEND(1, "待发货"),
    NORECEIVE(2, "待收货"),
    COMPLETED(3, "交易完成");

    private final int code;
    private final String message;

    OrderState(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public boolean isEquals(Integer state) {
        return state == null ? false : this.code == state;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
