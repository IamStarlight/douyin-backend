package com.bjtu.movie.utils;

import com.bjtu.movie.service.impl.AdminServiceImpl;
import com.bjtu.movie.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyInitializer implements CommandLineRunner {
    @Autowired
    private AdminServiceImpl adminService;

    @Override
    public void run(String... args) throws Exception {
        // 执行初始化逻辑
        adminService.initSuperAdmin();
    }
}
