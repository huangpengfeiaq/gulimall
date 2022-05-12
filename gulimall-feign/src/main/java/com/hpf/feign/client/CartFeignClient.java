package com.hpf.feign.client;

import com.hpf.common.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient("gulimall-cart")
public interface CartFeignClient {

    /**
     * 查询当前用户购物车选中的商品项
     */
    @GetMapping(value = "/currentUserCartItems")
    List<OrderItemVo> getCurrentCartItems();

}
