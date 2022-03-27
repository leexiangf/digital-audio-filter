package com.zj.service.impl;

import com.zj.enumc.ResultCode;
import com.zj.po.LoginUser;
import com.zj.po.User;
import com.zj.result.JsonResult;
import com.zj.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

/**
 * @ClassName : LoginServiceImp
 * @Description
 * @Date 2022/3/27 16:04
 * @Created lxf
 */

@Service
public class LoginServiceImp {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisTemplate redisTemplate;

    public JsonResult<HashMap> login(User user){
        //封装到这个类中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        //通过这个类获取到对应的信息
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        //认证不通过
        if(Objects.isNull(authentication)){
            new RuntimeException("登录失败");
        }
        //认证通过，生成一个token
        User users = (User)authentication.getPrincipal();
        String userId = users.getUserId();
        String token = JWTUtil.createToken(userId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);
        map.put("userInfo",users);
        //可以把token放入redis中，减少数据库压力
        redisTemplate.opsForValue().set(userId,users);
        return  new JsonResult<HashMap>(ResultCode.SUCCESS,map);

    }


    public JsonResult logout(){
        //获取securityContext中的值
        UsernamePasswordAuthenticationToken authentication =(UsernamePasswordAuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        //删除redis中的值
        redisTemplate.delete(loginUser.getUser().getUserId());
         return  new JsonResult(ResultCode.SUCCESS,"登出成功");
    }

}
