package com.zj.filter;

import com.zj.enumc.ResultCode;
import com.zj.exception.MyException;
import com.zj.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName : MyExceptionHandler
 * @Description  配置全局捕获异常
 * @Date 2022/3/25 0:25
 * @Created lxf
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

 /*   @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public JsonResult serverExceptionHandler(MyException e){
        log.error(e.getMessage());
        return new JsonResult(e.getCode(),e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResult defaultExceptionHandler(Exception e){
        log.error(e.getMessage());
        return new JsonResult(ResultCode.FAIL,e.getMessage());
    }*/
}
