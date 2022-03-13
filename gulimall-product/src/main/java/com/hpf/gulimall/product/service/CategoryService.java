package com.hpf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpf.common.utils.PageUtils;
import com.hpf.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-24 20:51:43
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);

    /**
     * 找到catelogId的完整路径：
     * [父/子/孙]
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);
}

