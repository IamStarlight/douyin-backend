package com.bjtu.douyin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bjtu.douyin.mapper")
public class DouyinApplication {

	public static void main(String[] args) {
		SpringApplication.run(DouyinApplication.class, args);
	}

}
