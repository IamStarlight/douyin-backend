package com.bjtu.douyin.utils;

import com.bjtu.douyin.controller.UserController;
import com.bjtu.douyin.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MyInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AdminServiceImpl adminService;

    @Override
    public void run(String... args) throws Exception {

        logger.info("Received request with input:");
        // 执行初始化逻辑
        adminService.initSuperAdmin();
    }
}
