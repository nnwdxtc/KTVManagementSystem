package com.ktv.controller;

import com.ktv.common.R;
import com.ktv.exception.BizException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BizException.class)
    public R<Void> handleBiz(BizException e) {
        return R.fail(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public R<Void> handleOther(Exception e) {
        e.printStackTrace();
        return R.fail("系统异常：" + e.getMessage());
    }
}