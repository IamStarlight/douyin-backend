package com.bjtu.movie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bjtu.movie.controller.dto.LoginDto;
import com.bjtu.movie.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jinxuan Chen
 * @since 2024-05-10
 */
public interface IUserService extends IService<User> {

    String getPermission(Integer id);

    void register(User newUser);

    void logout();

    HashMap<String,String> login(LoginDto dto);

    User getByName(String name);

    void resetPassword(Integer id, String password);

    void resetInfo(Integer id, User info);

    List<Map<String, Object>> getAllUser();

    Map<String, Object> getOneUser(Integer id);

    void deleteOneUser(Integer id);

    User getCurrentUser();
}
