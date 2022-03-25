package com.hpf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpf.common.utils.PageUtils;
import com.hpf.gulimall.product.entity.SpuInfoDescEntity;
import com.hpf.gulimall.product.entity.SpuInfoEntity;
import com.hpf.gulimall.product.vo.SpuSaveVO;

import java.util.Map;

/**
 * spu信息
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-24 20:51:44
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVO vo);

    void saveBaseSpuInfo(SpuInfoEntity infoEntity);

    PageUtils queryPageByCodition(Map<String, Object> params);
}

