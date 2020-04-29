package com.shop.bean;

import java.io.Serializable;

public class ResponseBody<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;
    private String msg;
    private T data;

    public ResponseBody(String msg) {
        super();
        this.code = 1;
        this.msg = msg;
    }

    public ResponseBody() {
        super();
        this.code = 0;
        this.msg = "OK";
    }

    public ResponseBody(T data) {
        super();
        this.code = 0;
        this.msg = "OK";
        this.data = data;
    }

    public static ResponseBody error(String msg) {
        return new ResponseBody(msg);
    }

    public static ResponseBody success(Object data) {
        return new ResponseBody(data);
    }

    public static ResponseBody notLogin() {
        ResponseBody res = new ResponseBody();
        res.setCode(2);
        res.setMsg("请登录");
        return res;
    }

    @Override
    public String toString() {
        return "ResponseBody [code=" + code + ", msg=" + msg + ", data=" + data + "]";
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
