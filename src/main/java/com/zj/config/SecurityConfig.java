package com.zj.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @ClassName : SecurityConfig
 * @Description
 * @Date 2022/3/21 19:44
 * @Created lxf
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //首页所有人可以访问
        http.authorizeRequests().antMatchers("/").permitAll()
                .antMatchers("/vip1/**").hasAnyRole("vip1")//相应权限对应相应界面
                .antMatchers("/vip2/**").hasAnyRole("vip2");
        //没有权限到登录页面
        http.formLogin().loginPage("/mylogin");
        //关闭cors
        http.cors().disable();
        //注销 并回到首页
        http.logout().logoutUrl("/");
        //记住我功能
        http.rememberMe();
    }

    //认证
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //内存读数据
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())//密码加密passwordEncoder(new BCryptPasswordEncoder())
                .withUser("laoda").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")
                .and().withUser("xiaodi").password(new BCryptPasswordEncoder().encode("112233")).roles("vip1");
        super.configure(auth);
    }
}
