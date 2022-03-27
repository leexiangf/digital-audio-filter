package com.zj.filter;

import com.alibaba.fastjson.JSON;
import com.zj.result.JsonResult;
import com.zj.utils.WebUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : AccessDeniedHandlerImp
 * @Description  授权异常处理
 * @Date 2022/3/27 18:44
 * @Created lxf
 */
@Component
public class AccessDeniedHandlerImp  implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        JsonResult result = new JsonResult<>(HttpStatus.FORBIDDEN.value(), "用户权限不足");
        String jsonString = JSON.toJSONString(result);
        //处理异常
        WebUtil.renderString(httpServletResponse,jsonString);
    }
}
