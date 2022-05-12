package com.hpf.gulimall.order.dao;

import com.hpf.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 订单
 * 
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-25 09:54:19
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {

    /**
     * 修改订单状态
     */
    void updateOrderStatus(@Param("orderSn") String orderSn,
                           @Param("code") Integer code,
                           @Param("payType") Integer payType);
}
