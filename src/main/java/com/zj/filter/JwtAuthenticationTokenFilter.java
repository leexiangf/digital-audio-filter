package com.zj.filter;

import com.mysql.jdbc.StringUtils;
import com.zj.po.LoginUser;
import com.zj.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @ClassName : JwtAuthenticationTokenFilter
 * @Description jwt 过滤器
 * @Date 2022/3/27 16:35
 * @Created lxf
 */
@Component
public class JwtAuthenticationTokenFilter  extends OncePerRequestFilter {


    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if(StringUtils.isNullOrEmpty(token)){
            //如果token为空，放行，后面再拦截
            filterChain.doFilter(request,response);
            return;
        }
        //解析token
        Claims claims = JWTUtil.parseToken(token);
        String userId = claims.getSubject();
        //从数据库获取到user信息
        LoginUser user = (LoginUser)redisTemplate.opsForValue().get(userId);
        if(Objects.isNull(user)){
            throw  new RuntimeException("用户未登录");
        }
        //存入securityContextHolder
        //封装到这个类中     获取权限信息user.getAuthorities()
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //放行
        filterChain.doFilter(request,response);

    }
}
