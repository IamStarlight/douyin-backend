package com.bjtu.movie.filter;

import com.alibaba.fastjson.JSONObject;
import com.bjtu.movie.model.LoginAdmin;
import com.bjtu.movie.exception.ServiceException;
import com.bjtu.movie.utils.JwtUtil;
import com.bjtu.movie.model.LoginUser;
import com.bjtu.movie.utils.RedisCache;
import com.bjtu.movie.utils.RegexUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("Token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }

        //解析token
        String userNameRole;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userNameRole = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(),"Token非法");
        }

        String userName = RegexUtil.parseUserName(userNameRole);

        //从redis中获取用户信息
        if(RegexUtil.matchUser(userNameRole)) {
            String redisKey = "user login:" + userName;
            JSONObject jsonObject = redisCache.getCacheObject(redisKey);
            LoginUser loginUser = jsonObject.toJavaObject(LoginUser.class);
            if (Objects.isNull(loginUser)) {
                throw new ServiceException(HttpStatus.NOT_FOUND.value(), "用户未登录");
            }
            //存入SecurityContextHolder
            //获取权限信息封装到Authentication中
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            request.setAttribute("CurrentUser",loginUser.getUser());
        }else{
            String redisKey = "admin login:" + userName;
            JSONObject jsonObject = redisCache.getCacheObject(redisKey);
            LoginAdmin loginAdmin = jsonObject.toJavaObject(LoginAdmin.class);
            if (Objects.isNull(loginAdmin)) {
                throw new ServiceException(HttpStatus.NOT_FOUND.value(), "用户未登录");
            }
            //存入SecurityContextHolder
            //获取权限信息封装到Authentication中
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            loginAdmin, null, loginAdmin.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        //放行
        filterChain.doFilter(request, response);
    }
}

