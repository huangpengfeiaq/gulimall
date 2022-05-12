package com.hpf.gulimall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpf.common.to.mq.SeckillOrderTo;
import com.hpf.common.utils.PageUtils;
import com.hpf.gulimall.order.entity.OrderEntity;
import com.hpf.gulimall.order.vo.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * 订单
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-25 09:54:19
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 订单确认页返回需要用的数据
     */
    OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException;

    /**
     * 创建订单
     */
    SubmitOrderResponseVo submitOrder(OrderSubmitVo vo);

    /**
     * 按照订单号获取订单信息
     */
    OrderEntity getOrderByOrderSn(String orderSn);

    /**
     * 关闭订单
     */
    void closeOrder(OrderEntity orderEntity);

    /**
     * 获取当前订单的支付信息
     */
    PayVo getOrderPay(String orderSn);

    /**
     * 查询当前用户所有订单数据
     */
    PageUtils queryPageWithItem(Map<String, Object> params);

    /**
     *支付宝异步通知处理订单数据
     */
    String handlePayResult(PayAsyncVo asyncVo);

    /**
     * 微信异步通知处理
     */
    String asyncNotify(String notifyData);


    /**
     * 创建秒杀单
     */
    void createSeckillOrder(SeckillOrderTo orderTo);
}

