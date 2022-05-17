package com.hpf.gulimall.ware;

import com.hpf.feign.client.MemberFeignClient;
import com.hpf.feign.client.OrderFeignClient;
import com.hpf.feign.client.ProductFeignClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients(clients = {MemberFeignClient.class, ProductFeignClient.class, OrderFeignClient.class})
@EnableTransactionManagement
@MapperScan("com.hpf.gulimall.ware.dao")
@EnableDiscoveryClient
@SpringBootApplication
public class GulimallWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallWareApplication.class, args);
    }

}
