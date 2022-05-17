package com.hpf.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpf.common.utils.PageUtils;
import com.hpf.gulimall.ware.entity.WareInfoEntity;
import com.hpf.gulimall.ware.vo.FareVo;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-25 09:56:32
 */
public interface WareInfoService extends IService<WareInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取运费和收货地址信息
     */
    FareVo getFare(Long addrId);
}

