package com.hpf.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpf.common.utils.PageUtils;
import com.hpf.common.vo.SocialUser;
import com.hpf.gulimall.member.entity.MemberEntity;
import com.hpf.gulimall.member.exception.PhoneException;
import com.hpf.gulimall.member.exception.UsernameException;
import com.hpf.gulimall.member.vo.MemberUserLoginVo;
import com.hpf.gulimall.member.vo.MemberUserRegisterVo;

import java.util.Map;

/**
 * 会员
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-25 09:45:47
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 用户注册
     */
    void register(MemberUserRegisterVo vo);

    /**
     * 判断邮箱是否重复
     */
    void checkPhoneUnique(String phone) throws PhoneException;

    /**
     * 判断用户名是否重复
     */
    void checkUserNameUnique(String userName) throws UsernameException;

    /**
     * 用户登录
     */
    MemberEntity login(MemberUserLoginVo vo);

    /**
     * 社交用户的登录
     */
    MemberEntity login(SocialUser socialUser) throws Exception;

    /**
     * 微信登录
     */
    MemberEntity login(String accessTokenInfo);
}

