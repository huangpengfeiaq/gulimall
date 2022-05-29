package com.hpf.gulimall.member;

import com.hpf.gulimall.feign.client.CouponFeignClient;
import com.hpf.gulimall.feign.client.OrderFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 1、spring-session依赖
 * 2、spring-session配置
 * 3、LoginInterceptor拦截器
 */
@EnableRedisHttpSession
@EnableFeignClients(clients = {CouponFeignClient.class, OrderFeignClient.class})
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemberApplication.class, args);
    }

}
