package com.hpf.gulimall.auth;

import com.hpf.gulimall.feign.client.MemberFeignClient;
import com.hpf.gulimall.feign.client.ThirdPartFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 核心原理：装饰者模式
 * 1）、@EnableRedisHttpSession导入RedisHttpSessionConfiguration配置
 * 1、给容器中添加了一个组件
 * RedisOperationsSessionRepository：Redis操作session，session的增删改查封装类
 */
@EnableRedisHttpSession     //整合Redis作为session存储
@EnableFeignClients(clients = {ThirdPartFeignClient.class, MemberFeignClient.class})
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallAuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallAuthServerApplication.class, args);
    }

}
