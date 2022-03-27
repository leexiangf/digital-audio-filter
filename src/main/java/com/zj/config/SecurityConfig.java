package com.zj.config;

import com.alibaba.fastjson.JSON;
import com.zj.enumc.ResultCode;
import com.zj.filter.*;
import com.zj.result.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName : SecurityConfig
 * @Description
 * @Date 2022/3/21 19:44
 * @Created lxf
 */
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private  JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthenticationEntryPointHandler authenticationEntryPointHandler;

    @Autowired
    private AccessDeniedHandlerImp accessDeniedHandlerImp;

    @Autowired
    private AuthenticationSuccessHandlerImp appLoginInSuccessHandler;

    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    MyUsernamePasswordAuthenticationFilter myAuthenticationFilter() throws Exception {
        MyUsernamePasswordAuthenticationFilter filter = new MyUsernamePasswordAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(appLoginInSuccessHandler);
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                                AuthenticationException e) throws IOException {
                response.setContentType("application/json;charset=utf-8");
                try {
                    response.getWriter().write(JSON.toJSONString(new JsonResult<>(ResultCode.EXCEPTION)));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        return filter;
    }

    //授权
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //放行登录接口 user/login   anonymous()只能未登录的状态访问
        http.authorizeRequests().antMatchers("/login").anonymous()
                //permitAll() 所有人能访问（登录和未登录）
                .antMatchers("/index").permitAll()
                //相应权限对应相应界面
                //.antMatchers("/vip1/**").hasAnyRole("vip1")
                //.antMatchers("/vip2/**").hasAnyRole("vip2")
                //除上述外所有请求都要认证
                .anyRequest().authenticated();
        //没有权限到登录页面
        http.formLogin().loginPage("/login");
        //csrf              关闭session获取到securityContext
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //注销 并回到首页
        http.logout().logoutUrl("/");
        //记住我功能
        http.rememberMe();
        //把自定义的jwt过滤器 放在 UsernamePasswordAuthenticationFilter 之前执行
        http.addFilterBefore(myAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                //认证失败异常处理
                .authenticationEntryPoint(authenticationEntryPointHandler)
                //授权失败异常处理
                .accessDeniedHandler(accessDeniedHandlerImp);
        //允许跨域
        http.cors();
    }
    //认证
 /*   @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //内存读数据
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())//密码加密passwordEncoder(new BCryptPasswordEncoder())
                .withUser("laoda").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")
                .and().withUser("xiaodi").password(new BCryptPasswordEncoder().encode("112233")).roles("vip1");
        super.configure(auth);
    }*/
}
