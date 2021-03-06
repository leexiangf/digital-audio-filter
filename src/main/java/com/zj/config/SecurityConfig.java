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

    //??????
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //?????????????????? user/login   anonymous()??????????????????????????????
        http.authorizeRequests().antMatchers("/login").anonymous()
                //permitAll() ??????????????????????????????????????????
                .antMatchers("/index").permitAll()
                //??????????????????????????????
                //.antMatchers("/vip1/**").hasAnyRole("vip1")
                //.antMatchers("/vip2/**").hasAnyRole("vip2")
                //????????????????????????????????????
                .anyRequest().authenticated();
        //???????????????????????????
        http.formLogin().loginPage("/login");
        //csrf              ??????session?????????securityContext
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //?????? ???????????????
        http.logout().logoutUrl("/");
        //???????????????
        http.rememberMe();
        //???????????????jwt????????? ?????? UsernamePasswordAuthenticationFilter ????????????
        http.addFilterBefore(myAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
                //????????????????????????
                .authenticationEntryPoint(authenticationEntryPointHandler)
                //????????????????????????
                .accessDeniedHandler(accessDeniedHandlerImp);
        //????????????
        http.cors();
    }
    //??????
 /*   @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //???????????????
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())//????????????passwordEncoder(new BCryptPasswordEncoder())
                .withUser("laoda").password(new BCryptPasswordEncoder().encode("123456")).roles("vip2","vip3")
                .and().withUser("xiaodi").password(new BCryptPasswordEncoder().encode("112233")).roles("vip1");
        super.configure(auth);
    }*/
}
