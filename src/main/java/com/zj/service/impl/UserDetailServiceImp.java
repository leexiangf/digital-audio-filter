package com.zj.service.impl;

import com.zj.mapper.UserInfoMapper;
import com.zj.po.LoginUser;
import com.zj.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * @ClassName : UserDetailServiceImp
 * @Description  封装自己的信息类  （UserDetailsService 是security自己的用户信息）
 * @Date 2022/3/27 14:45
 * @Created lxf
 */
@Service
@Slf4j
public class UserDetailServiceImp  implements UserDetailsService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("username:{}",username);
        User user = userInfoMapper.selectByUserName(username);
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        //封装权限信息 这里模拟数据库通过userId查询出来的权限集合
        ArrayList<String> strings = new ArrayList<>(Arrays.asList("test", "admin"));
        return new LoginUser(user,strings);
    }
}
