package com.ktv.common;

public class R<T> {
    private boolean success;
    private String message;
    private T data;
    private int code;

    public R() {}

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.success = true;
        r.message = "ok";
        r.data = data;
        r.code = 200;
        return r;
    }

    public static <T> R<T> ok(T data, String message) {
        R<T> r = new R<>();
        r.success = true;
        r.message = message;
        r.data = data;
        r.code = 200;
        return r;
    }

    public static <T> R<T> fail(String msg) {
        return fail(500, msg);
    }

    public static <T> R<T> fail(int code, String msg) {
        R<T> r = new R<>();
        r.success = false;
        r.message = msg;
        r.code = code;
        return r;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
}