package com.hpf.gulimall.order.service.impl;

import com.hpf.common.to.mq.SeckillOrderTo;
import com.hpf.gulimall.order.vo.*;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpf.common.utils.PageUtils;
import com.hpf.common.utils.Query;

import com.hpf.gulimall.order.dao.OrderDao;
import com.hpf.gulimall.order.entity.OrderEntity;
import com.hpf.gulimall.order.service.OrderService;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        return null;
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        return null;
    }

    @Override
    public void closeOrder(OrderEntity orderEntity) {

    }

    @Override
    public PayVo getOrderPay(String orderSn) {
        return null;
    }

    @Override
    public PageUtils queryPageWithItem(Map<String, Object> params) {
        return null;
    }

    @Override
    public String handlePayResult(PayAsyncVo asyncVo) {
        return null;
    }

    @Override
    public String asyncNotify(String notifyData) {
        return null;
    }

    @Override
    public void createSeckillOrder(SeckillOrderTo orderTo) {

    }

}