package com.hpf.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpf.common.to.SkuHasStockVo;
import com.hpf.common.utils.PageUtils;
import com.hpf.gulimall.ware.entity.WareSkuEntity;
import com.hpf.gulimall.ware.vo.WareSkuLockVo;

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

    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

