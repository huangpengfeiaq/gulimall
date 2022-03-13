package com.hpf.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 品牌分类关联
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2022-03-13 16:58:23
 */
@Data
@TableName("pms_category_brand_relation")
public class CategoryBrandRelationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Long id;
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 分类id
     */
    private Long catelogId;
    /**
     * 品牌名
     */
    private String brandName;
    /**
     * 分类名
     */
    private String catelogName;

    public CategoryBrandRelationEntity() {
    }

    public CategoryBrandRelationEntity(BrandEntity brand) {
        this.brandId = brand.getBrandId();
        this.brandName = brand.getName();
    }

    public CategoryBrandRelationEntity(CategoryEntity category) {
        this.catelogId = category.getCatId();
        this.catelogName = category.getName();
    }
}
