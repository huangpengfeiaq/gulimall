package com.hpf.gulimall.search;

import com.hpf.feign.client.ProductFeignClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@EnableRedisHttpSession
@EnableFeignClients(clients = {ProductFeignClient.class})
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class GulimallSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(GulimallSearchApplication.class, args);
    }
}
