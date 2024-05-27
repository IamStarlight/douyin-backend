package com.bjtu.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bjtu.movie.controller.dto.LoginDto;
import com.bjtu.movie.entity.User;
import com.bjtu.movie.constants.Role;
import com.bjtu.movie.exception.ServiceException;
import com.bjtu.movie.mapper.UserMapper;
import com.bjtu.movie.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjtu.movie.utils.DateTimeUtil;
import com.bjtu.movie.utils.JwtUtil;
import com.bjtu.movie.model.LoginUser;
import com.bjtu.movie.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jinxuan Chen
 * @since 2024-05-10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

    @Override
    public String getPermission(Integer id){
        return getById(id).getPermission();
    }

    @Override
    public void register(User newUser) {
        if(getByName(newUser.getName()) != null) {
            throw new ServiceException(HttpStatus.FORBIDDEN.value(), "用户名已存在");
        }
        newUser.setPassword(encodePassword(newUser.getPassword()));
        newUser.setCreatedAt(DateTimeUtil.createNowTimeString());
        newUser.setPermission(Role.ROLE_USER.getValue());
        newUser.setDeleted(false);
        save(newUser);
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String userName = loginUser.getUser().getName();
        redisCache.deleteObject("user login:" + userName);
    }

    @Override
    public HashMap<String,String> login(LoginDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getName()+Role.ROLE_USER.getValue(), dto.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authenticate)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误");
        }

        //使用userid生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userName = loginUser.getUser().getName();
        String jwt = JwtUtil.createJWT(userName,Role.ROLE_USER.getValue());

        //authenticate存入redis
        redisCache.setCacheObject("user login:"+userName,loginUser);

        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        User nowUser = loginUser.getUser();
        map.put("token",jwt);
//        map.put("permission",nowUser.getPermission());
        map.put("name", nowUser.getName());
//        map.put("id", nowUser.getId().toString());

        return map;
    }

    @Override
    public User getByName(String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getName,name)
                .eq(User::isDeleted,false);
        return getOne(wrapper);
    }

    private String encodePassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public void resetPassword(Integer id, String password) {
        //todo：验证
        User user = new User();
        user.setId(id);
        user.setPassword(encodePassword(password));
        updateById(user);
    }

    @Override
    public void resetInfo(Integer id, User info) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId,id)
                .eq(User::isDeleted,false);
        if(getOne(wrapper) == null){
            throw new ServiceException(HttpStatus.NOT_FOUND.value(),"用户不存在");
        }
        info.setId(id);
        updateById(info);
    }

    @Override
    public List<Map<String, Object>> getAllUser() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::isDeleted,false)
                .select(User::getId,User::getName);
        return userMapper.selectMaps(wrapper);
    }

    @Override
    public Map<String, Object> getOneUser(Integer id) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::isDeleted,false)
                .eq(User::getId,id)
                .select(User::getId,User::getName);
        return userMapper.selectMaps(wrapper).get(0);
    }

    @Override
    public void deleteOneUser(Integer id) {
        if(getOneUser(id) == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND.value(), "用户不存在");
        }
        LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(User::getId,id)
                .set(User::isDeleted,true);
        update(wrapper);
    }


    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return loginUser.getUser();
    }
}
