package com.hpf.gulimall.product;

import com.hpf.feign.client.CouponClient;
import com.hpf.feign.client.SearchClient;
import com.hpf.feign.client.WareClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(clients = {CouponClient.class, WareClient.class, SearchClient.class})
@MapperScan("com.hpf.gulimall.product.dao")
@SpringBootApplication
@EnableDiscoveryClient
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
