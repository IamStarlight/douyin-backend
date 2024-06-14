## 简单介绍

- `spring boot`：
    - `spring-boot-starter-web`：web 组件
    - `spring-boot-starter-test`： 单元测试组件
- `mybatis plus`：ORM框架，比通用 Mapper 好用
- `hutool-all`：小而全的 Java 工具类库
- `javamelody-spring-boot-starter`：监控器，打印 http 请求 url，响应时间
- `lombok`：Java 实用工具，必备插件
- `validator`：参数校验相关的注解

## 如何使用

创建一个名称为：`scaffold` 的数据库，执行 sql：

```sql
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                      `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                      `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
                      `age` int unsigned NOT NULL COMMENT '年龄',
                      `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
INSERT INTO user (name, age, email) VALUES
('Jone', 18, 'test1@baomidou.com'),
('Jack', 20, 'test2@baomidou.com'),
('Tom', 28, 'test3@baomidou.com'),
('Sandy', 21, 'test4@baomidou.com'),
('Billie', 24, 'test5@baomidou.com');
```

`application-*.yml`配置文件中，修改数据库配置：

```
# 数据库
spring:
  datasource:
    driverClassName: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/scaffold?useUnicode=true&useSSL=true&autoReconnect=true&allowMultiQueries=true
    username: root
    password: root
```

修改dev环境的日志级别目录：
```
# 日志级别
logging:
  level:
    com.leeyom.scaffold.mapper: debug
```



最后访问：[http://127.0.0.1:9380/scaffold/user/selectAll](http://127.0.0.1:9380/scaffold/user/selectAll) 测试下。



## 推荐

IDEA 结合插件 [api-generator](https://github.com/Forgus/api-generator) 可以一键生成接口文档，无代码入侵，虽然 Swagger 也可以，但是需要额外引入相关依赖，同时代码侵入性太强了（需要增加注解）。
