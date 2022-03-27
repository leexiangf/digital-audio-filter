package com.zj.controller;

import com.zj.po.User;
import com.zj.result.JsonResult;
import com.zj.service.impl.LoginServiceImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @ClassName : LoginController
 * @Description 登录
 * @Date 2022/3/27 13:19
 * @Created lxf
 */
@Slf4j
@RestController
//@PreAuthorize("hasAuthority(test)")
public class LoginController {

    @Autowired
    private LoginServiceImp loginServiceImp;

    @GetMapping("/hello")
    //@PreAuthorize("hasAuthority(test)")
    public String hello(){
        return "hello";
    }

    @PostMapping("/user/login")
    public JsonResult login(@RequestBody User user){
        JsonResult<HashMap> userInfoJsonResult = loginServiceImp.login(user);
        return userInfoJsonResult;
    }

    @PostMapping("/user/logout")
    public JsonResult logout(@RequestBody User user){
        JsonResult<HashMap> userInfoJsonResult = loginServiceImp.login(user);
        return null;
    }

}
