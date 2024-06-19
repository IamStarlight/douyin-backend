package com.bjtu.douyin.controller;


import com.bjtu.douyin.annotation.CurrentUser;
import com.bjtu.douyin.entity.Admin;
import com.bjtu.douyin.entity.User;
import com.bjtu.douyin.service.impl.AdminServiceImpl;
import com.bjtu.douyin.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员管理
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminServiceImpl adminService;

    /**
     * 注册管理员
     * @param admin
     * @return
     * @throws Exception
     */
    @PostMapping
    //@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Result> adminRegister(@RequestBody Admin admin) {
        adminService.adminRegister(admin);
        // 返回状态码201 用户新建或修改数据成功
        return new ResponseEntity<>(Result.success(), HttpStatus.CREATED);
    }

    /**
     * 获取全部管理员
     * @return
     */
    @GetMapping
    //@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Result> getAllAdmin() {
        return new ResponseEntity<>(Result.success(adminService.getAllAdmin()), HttpStatus.OK);
    }

    /**
     * 获取一个管理员
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Result> getOneAdmin(@PathVariable Integer id){
        return new ResponseEntity<>(Result.success(adminService.getOneAdmin(id)), HttpStatus.OK);
    }

    /**
     * 重置管理员密码
     * @param id
     * @param password
     * @return
     */
    @PutMapping("/security/{id}")
    //@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN','ROLE_ADMIN')")
    public ResponseEntity<Result> resetPassword(@PathVariable Integer id, @RequestParam String password) {
        adminService.resetPassword(id,password);
        return new ResponseEntity<>(Result.success(), HttpStatus.CREATED);
    }

    /**
     * 重置管理员信息（不包括密码）
     * @param user
     * @param info
     * @return
     */
    @PutMapping
    //@PreAuthorize("hasAnyAuthority('ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_USER')")
    public ResponseEntity<Result> updateUserInfo(@CurrentUser User user, @RequestBody Admin info){
        adminService.resetInfo(user.getId(),info);
        return new ResponseEntity<>((Result.success()),HttpStatus.CREATED);
    }

    /**
     * 删除一个管理员
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Result> deleteOneAdmin(@PathVariable Integer id) {
        adminService.deleteOneAdmin(id);
        return new ResponseEntity<>(Result.success(), HttpStatus.NO_CONTENT);
    }

}

