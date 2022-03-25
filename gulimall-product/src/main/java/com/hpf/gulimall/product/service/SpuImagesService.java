package com.hpf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpf.common.utils.PageUtils;
import com.hpf.gulimall.product.entity.SpuImagesEntity;
import com.hpf.gulimall.product.vo.Images;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-24 20:51:44
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long spuId, List<String> images);
}

