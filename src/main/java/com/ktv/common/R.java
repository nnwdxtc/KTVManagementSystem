package com.ktv.common;

public class R<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.success = true;
        r.message = "ok";
        r.data = data;
        return r;
    }
    public static <T> R<T> fail(String msg) {
        R<T> r = new R<>();
        r.success = false;
        r.message = msg;
        return r;
    }
    // getter/setter 省略
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}