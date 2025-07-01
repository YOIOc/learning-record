package com.itheima.exception;

import com.itheima.pojo.Result;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 设置当出现何种异常时，使用该处理器方法
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){  // 参数Exception用于接收异常对象
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败");
    }
}
