package com.hpf.gulimall.feign.client;

import com.hpf.common.utils.R;
import com.hpf.common.vo.MemberAddressVo;
import com.hpf.common.vo.SocialUser;
import com.hpf.common.vo.UserLoginVo;
import com.hpf.common.vo.UserRegisterVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("gulimall-member")
public interface MemberFeignClient {
    @PostMapping(value = "/member/member/register")
    R register(@RequestBody UserRegisterVo vo);

    @PostMapping(value = "/member/member/login")
    R login(@RequestBody UserLoginVo vo);

    @PostMapping(value = "/member/member/oauth2/login")
    R oauthLogin(@RequestBody SocialUser socialUser) throws Exception;

    @PostMapping(value = "/member/member/weixin/login")
    R weixinLogin(@RequestParam("accessTokenInfo") String accessTokenInfo);

    /**
     * 查询当前用户的全部收货地址
     */
    @GetMapping(value = "/member/memberreceiveaddress/{memberId}/address")
    List<MemberAddressVo> getAddress(@PathVariable("memberId") Long memberId);

    /**
     * 根据id获取用户地址信息
     */
    @RequestMapping("/member/memberreceiveaddress/info/{id}")
    R info(@PathVariable("id") Long id);
}
