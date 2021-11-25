package com.hpf.feign.client;

import com.hpf.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-coupon")
public interface CouponClient {
    @RequestMapping("coupon/coupon/member/list")
    R memberCoupons();
}
