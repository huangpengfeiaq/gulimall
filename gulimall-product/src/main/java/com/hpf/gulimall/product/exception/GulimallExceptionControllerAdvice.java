package com.hpf.gulimall.product.exception;

import com.hpf.common.exception.BizCodeEnume;
import com.hpf.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理所有异常
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.hpf.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题：{}，异常类型：{}", e.getMessage(), e.getClass());
        Map<String, String> map = new HashMap<>();
        //1.获取校验的错误结果
        e.getBindingResult().getFieldErrors().forEach((item) -> {
            //FieldError 获取到错误提示
            String message = item.getDefaultMessage();
            //获取错误的属性名字
            String field = item.getField();
            map.put(field, message);
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        log.error("未知异常：{}，异常类型：{}", throwable.getMessage(), throwable.getClass());
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
