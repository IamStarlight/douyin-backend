package com.bjtu.douyin.utils;

import com.bjtu.douyin.service.OssLoggingSerice;
import com.bjtu.douyin.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyInitializer implements CommandLineRunner {
    private static final String ENDPOINT = "";
    private static final String BUCKET_NAME = "";
    private static final String ACCESS_KEY_ID = "";
    private static final String ACCESS_KEY_SECRET = "";
    private static final String TARGET_BUCKET_NAME = "";
    private static final String TARGET_PREFIX = "";

    @Autowired
    private AdminServiceImpl adminService;

    @Override
    public void run(String... args) throws Exception {
        // 执行初始化逻辑
        adminService.initSuperAdmin();
        OssLoggingSerice.enableBucketLogging(BUCKET_NAME, TARGET_BUCKET_NAME, TARGET_PREFIX);
    }
}
