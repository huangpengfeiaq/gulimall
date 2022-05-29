package com.hpf.gulimall.feign.fallback;

import com.hpf.common.exception.BizCodeEnum;
import com.hpf.common.utils.R;
import com.hpf.gulimall.feign.client.SeckillFeignClient;
import org.springframework.stereotype.Component;

@Component
public class SeckillFeignClientFallBack implements SeckillFeignClient {
    @Override
    public R getSkuSeckilInfo(Long skuId) {
        System.out.println("熔断方法调用成功。。");
        return R.error(BizCodeEnum.TO_MANY_REQUEST.getCode(),BizCodeEnum.TO_MANY_REQUEST.getMessage());
    }
}
