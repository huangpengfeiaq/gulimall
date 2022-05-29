package com.hpf.gulimall.feign.client;

import com.hpf.common.utils.R;
import com.hpf.common.vo.PayVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("gulimall-third-party")
public interface ThirdPartFeignClient {

    @GetMapping(value = "/sms/sendCode")
    R sendCode(@RequestParam("phone") String phone, @RequestParam("code") String code);

    @GetMapping(value = "/pay",consumes = "application/json")
    String pay(@RequestBody PayVo vo) throws Exception;
}
