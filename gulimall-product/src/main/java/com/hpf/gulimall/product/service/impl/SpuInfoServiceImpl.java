package com.hpf.gulimall.product.service.impl;

import com.hpf.common.utils.R;
import com.hpf.feign.client.CouponClient;
import com.hpf.common.to.SkuReductionTO;
import com.hpf.common.to.SpuBoundTO;
import com.hpf.gulimall.product.entity.*;
import com.hpf.gulimall.product.service.*;
import com.hpf.gulimall.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpf.common.utils.PageUtils;
import com.hpf.common.utils.Query;

import com.hpf.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private SpuImagesService imagesService;
    @Resource
    private AttrService attrService;
    @Resource
    private ProductAttrValueService attrValueService;
    @Resource
    private SkuInfoService skuInfoService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private CouponClient couponClient;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    //TODO 待高级部分完善
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVO vo) {
        //1.保存spu基本信息 .pms_sku_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);

        //2.保存spu的描述图片 .pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(infoEntity.getId());
        descEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);

        //3.保存spu的图片集 .pms_spu_images
        List<String> images = vo.getImages();
        imagesService.saveImages(infoEntity.getId(), images);

        //4.保存spu的规格参数 .pms_product_attr_value
        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();
        List<ProductAttrValueEntity> collect = baseAttrs.stream().map(attr -> {
            ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
            valueEntity.setAttrId(attr.getAttrId());
            valueEntity.setAttrName(attrService.getById(attr.getAttrId()).getAttrName());
            valueEntity.setAttrValue(attr.getAttrValues());
            valueEntity.setQuickShow(attr.getShowDesc());
            valueEntity.setSpuId(infoEntity.getId());
            return valueEntity;
        }).collect(Collectors.toList());
        attrValueService.saveProductAttr(collect);

        //5.保存spu的积分信息 gulimall_sms.sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBoundTO spuBoundTO = new SpuBoundTO();
        BeanUtils.copyProperties(bounds, spuBoundTO);
        spuBoundTO.setSpuId(infoEntity.getId());
        R r = couponClient.saveSpuBounds(spuBoundTO);
        if (r.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }

        //5.保存当前spu对应的所有sku信息
        //5.1 sku基本信息 .pms_sku_info
        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach(item -> {
                String defaultImg = "";
                for (Images img : item.getImages()) {
                    if (img.getDefaultImg() == 1) {
                        defaultImg = img.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.saveSkuInfo(skuInfoEntity);
                Long skuId = skuInfoEntity.getSkuId();

                //5.2 sku的图片信息 .pms_sku_images
                List<SkuImagesEntity> imagesEntities = item
                        .getImages()
                        .stream()
                        .map(img -> {
                            SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                            skuImagesEntity.setSkuId(skuId);
                            skuImagesEntity.setImgUrl(img.getImgUrl());
                            skuImagesEntity.setDefaultImg(img.getDefaultImg());
                            return skuImagesEntity;
                        })
                        .filter(img -> !StringUtils.isEmpty(img.getImgUrl()))
                        .collect(Collectors.toList());
                skuImagesService.saveBatch(imagesEntities);

                //5.3 sku的销售属性信息 .pms_sku_sale_attr_value
                List<Attr> attr = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attr.stream().map(a -> {
                    SkuSaleAttrValueEntity attrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(a, attrValueEntity);
                    attrValueEntity.setSkuId(skuId);
                    return attrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //5.4 sku的优惠信、满减信息 gulimall_sms.sms_sku_ladder
                //gulimall_sms.sms_sku_full_reduction
                //gulimall_sms.sms_member_price
                SkuReductionTO skuReductionTO = new SkuReductionTO();
                BeanUtils.copyProperties(item, skuReductionTO);
                skuReductionTO.setSkuId(skuId);
                if (skuReductionTO.getFullCount() > 0 || skuReductionTO.getFullPrice().compareTo(new BigDecimal(0)) > 0) {
                    R r1 = couponClient.saveSkuReduction(skuReductionTO);
                    if (r1.getCode() != 0) {
                        log.error("远程保存sku的优惠信、满减信息失败");
                    }
                }
            });
        }
    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        baseMapper.insert(infoEntity);
    }


}