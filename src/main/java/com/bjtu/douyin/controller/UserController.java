package com.bjtu.douyin.controller;


import com.bjtu.douyin.entity.User;
import com.bjtu.douyin.service.impl.UserServiceImpl;
import com.bjtu.douyin.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户管理
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserServiceImpl userService;

    /**
     * 重置用户密码
     * @param id
     * @param password
     * @return
     */
    @PutMapping("/security/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Result> updateUserPassword(@PathVariable Integer id,@RequestParam String password){

        userService.resetPassword(id,password);
        return new ResponseEntity<>((Result.success()),HttpStatus.CREATED);
    }

    /**
     * 重置用户信息（不包括密码）
     * @param id
     * @param info
     * @return
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Result> updateUserInfo(@PathVariable Integer id, @RequestBody User info){
        userService.resetInfo(id,info);
        return new ResponseEntity<>((Result.success()),HttpStatus.CREATED);
    }

    /**
     * 获取全部用户
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<Result> getAllUser(){
        return new ResponseEntity<>(Result.success(userService.getAllUser()), HttpStatus.OK);
    }

    /**
     * 获取一个用户
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<Result> getOneUser(@PathVariable Integer id){
        return new ResponseEntity<>(Result.success(userService.getOneUser(id)), HttpStatus.OK);
    }

    /**
     * 删除一个用户
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<Result> deleteOneUser(@PathVariable Integer id) {
        userService.deleteOneUser(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.NO_CONTENT);
    }

}
