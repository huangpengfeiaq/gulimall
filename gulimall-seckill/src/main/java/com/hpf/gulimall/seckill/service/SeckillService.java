package com.hpf.gulimall.seckill.service;

import com.hpf.gulimall.seckill.to.SeckillSkuRedisTo;

import java.util.List;

public interface SeckillService {

    /**
     * 上架三天需要秒杀的商品
     */
    void uploadSeckillSkuLatest3Days();

    List<SeckillSkuRedisTo> getCurrentSeckillSkus();

    /**
     * 根据skuId查询商品是否参加秒杀活动
     */
    SeckillSkuRedisTo getSkuSeckilInfo(Long skuId);

    /**
     * 当前商品进行秒杀（秒杀开始）
     */
    String kill(String killId, String key, Integer num) throws InterruptedException;
}
