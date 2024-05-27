package com.bjtu.movie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bjtu.movie.controller.dto.LoginDto;
import com.bjtu.movie.entity.Admin;
import com.bjtu.movie.constants.Role;
import com.bjtu.movie.model.LoginAdmin;
import com.bjtu.movie.exception.ServiceException;
import com.bjtu.movie.mapper.AdminMapper;
import com.bjtu.movie.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjtu.movie.utils.DateTimeUtil;
import com.bjtu.movie.utils.JwtUtil;
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
 * 管理员表 服务实现类
 * </p>
 *
 * @author Jinxuan Chen
 * @since 2024-05-11
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public void initSuperAdmin() {
        //如果不存在超级管理员，创建一个
        if(!hasSuperAdmin()) {
            Admin superAdmin = new Admin();
            superAdmin.setName("超级管理员");
            superAdmin.setPassword(encodePassword("123"));
            superAdmin.setPermission(Role.ROLE_SUPER_ADMIN.getValue());
            superAdmin.setCreatedAt(DateTimeUtil.createNowTimeString());
            save(superAdmin);
        }
    }

    private String encodePassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean hasSuperAdmin(){
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getPermission, Role.ROLE_SUPER_ADMIN.getValue())
                .eq(Admin::isDeleted,false);
        Admin superAdmin = getOne(wrapper);
        return superAdmin != null;
    }

    @Override
    public List<Map<String, Object>> getAllAdmin() {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getPermission, Role.ROLE_ADMIN.getValue())
                .eq(Admin::isDeleted,false)
                .select(Admin::getId,Admin::getName);
        return adminMapper.selectMaps(wrapper);
    }

    @Override
    public Map<String, Object> getOneAdmin(Integer id) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getPermission, Role.ROLE_ADMIN.getValue())
                .eq(Admin::getId,id)
                .eq(Admin::isDeleted,false)
                .select(Admin::getId,Admin::getName);
        return adminMapper.selectMaps(wrapper).get(0);
    }

    @Override
    public void deleteOneAdmin(Integer id) {
        if(getOneAdmin(id) == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND.value(), "管理员不存在");
        }
        LambdaUpdateWrapper<Admin> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Admin::getId,id)
                .set(Admin::isDeleted,true);
        update(wrapper);
    }

    @Override
    public void resetPassword(Integer id, String password) {
        //todo：验证
        if(getOneAdmin(id) == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND.value(), "管理员不存在");
        }
        Admin admin = new Admin();
        admin.setId(id);
        admin.setPassword(encodePassword(password));
        updateById(admin);
    }

    @Override
    public void resetInfo(Integer id, Admin info) {
        if(getOneAdmin(id) == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND.value(), "管理员不存在");
        }
        info.setId(id);
        updateById(info);
    }

    @Override
    public Admin getByName(String name){
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getName,name)
                .eq(Admin::isDeleted,false);
        return getOne(wrapper);
    }

    @Override
    public void adminRegister(Admin newAdmin) {
        if(getByName(newAdmin.getName()) != null) {
            throw new ServiceException(HttpStatus.FORBIDDEN.value(), "用户名已存在");
        }
        newAdmin.setPassword(encodePassword(newAdmin.getPassword()));
        newAdmin.setCreatedAt(DateTimeUtil.createNowTimeString());
        newAdmin.setPermission(Role.ROLE_ADMIN.getValue());
        newAdmin.setDeleted(false);
        save(newAdmin);
    }

    @Override
    public HashMap<String,String> loginAdmin(LoginDto dto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(dto.getName()+Role.ROLE_ADMIN.getValue(), dto.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if(Objects.isNull(authenticate)) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误");
        }

        //使用userid生成token
        LoginAdmin loginAdmin = (LoginAdmin) authenticate.getPrincipal();
        String adminName = loginAdmin.getAdmin().getName();
        String jwt = JwtUtil.createJWT(adminName,Role.ROLE_ADMIN.getValue());

        //authenticate存入redis
        redisCache.setCacheObject("admin login:"+adminName,loginAdmin);

        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        Admin nowAdmin = loginAdmin.getAdmin();
        map.put("token",jwt);
//        map.put("permission",nowAdmin.getPermission());
        map.put("name", nowAdmin.getName());
//        map.put("id", nowAdmin.getId().toString());

        return map;
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginAdmin loginAdmin = (LoginAdmin) authentication.getPrincipal();
        String adminName = loginAdmin.getAdmin().getName();
        redisCache.deleteObject("admin login:" + adminName);
    }
}
