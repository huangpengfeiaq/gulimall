package com.hpf.gulimall.coupon.dao;

import com.hpf.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-25 09:39:24
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
