package com.hpf.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpf.common.to.OrderTo;
import com.hpf.common.to.SkuHasStockVo;
import com.hpf.common.to.mq.StockLockedTo;
import com.hpf.common.utils.PageUtils;
import com.hpf.common.vo.WareSkuLockVo;
import com.hpf.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-25 09:56:32
 */
public interface WareSkuService extends IService<WareSkuEntity> {
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 添加库存
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    /**
     * 判断是否有库存
     */
    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);

    /**
     * 锁定库存
     */
    boolean orderLockStock(WareSkuLockVo vo);

    /**
     * 解锁库存
     */
    void unlockStock(StockLockedTo to);

    /**
     * 解锁订单
     */
    void unlockStock(OrderTo orderTo);
}

