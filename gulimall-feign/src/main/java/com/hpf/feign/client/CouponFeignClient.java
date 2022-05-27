package com.hpf.feign.client;

import com.hpf.common.utils.R;
import com.hpf.common.to.SkuReductionTO;
import com.hpf.common.to.SpuBoundTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-coupon")
public interface CouponFeignClient {
    @RequestMapping("coupon/coupon/member/list")
    R memberCoupons();

    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTO spuBoundTO);

    @PostMapping("coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTO skuReductionTO);

    /**
     * 查询最近三天需要参加秒杀商品的信息
     */
    @GetMapping(value = "/coupon/seckillsession/Lates3DaySession")
    R getLates3DaySession();
}
