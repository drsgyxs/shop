package com.shop.enums;

public enum CommodityOrder {
    SALES(1, "销量"),
    NEWER(2, "新品优先"),
    PRICEASC(3, "价格升序"),
    PRICEDESC(4, "价格降序");

    private final int code;
    private final String message;

    CommodityOrder(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public boolean isEquals(Integer code) {
        return code != null ? this.code == code : false;
    }

}
