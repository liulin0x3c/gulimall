package com.liulin.product.exception;


import com.liulin.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.liulin.product.controller")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public R handleValidException(MethodArgumentNotValidException e) {
        val bindingResult = e.getBindingResult();
        Map<String, String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((item)->{
            String message = item.getDefaultMessage();
            String field = item.getField();
            map.put(field, message);
        });
        return R.error(400, "提交的数据不合法").put("data", map);
    }
}
