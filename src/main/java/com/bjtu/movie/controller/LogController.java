package com.bjtu.movie.controller;

import com.bjtu.movie.controller.dto.LoginDto;
import com.bjtu.movie.entity.User;
import com.bjtu.movie.service.impl.AdminServiceImpl;
import com.bjtu.movie.service.impl.UserServiceImpl;
import com.bjtu.movie.model.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 登陆管理
 */
@RestController
public class LogController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private AdminServiceImpl adminService;

    /**
     * 注册用户
     * @param newUser
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<Result> register(@RequestBody @Valid User newUser){
        userService.register(newUser);
        return new ResponseEntity<>((Result.success()), HttpStatus.OK);
    }

    /**
     * 登录用户
     * @param dto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<Result> login(@RequestBody @Valid LoginDto dto){
        return new ResponseEntity<>(Result.success(userService.login(dto)), HttpStatus.OK);
    }

    /**
     * 登录管理员
     * @param dto
     * @return
     */
    @PostMapping("/admin/login")
    public ResponseEntity<Result> loginAdmin(@RequestBody @Valid LoginDto dto){
        return new ResponseEntity<>(Result.success(adminService.loginAdmin(dto)), HttpStatus.OK);
    }

    /**
     * 登出用户
     * @return
     */
    @PostMapping("/user/logout")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<Result> logout(){
        userService.logout();
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }

    /**
     * 登出管理员
     * @return
     */
    @PostMapping("/admin/logout")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<Result> logoutAdmin(){
        adminService.logout();
        return new ResponseEntity<>(Result.success(), HttpStatus.OK);
    }
}
