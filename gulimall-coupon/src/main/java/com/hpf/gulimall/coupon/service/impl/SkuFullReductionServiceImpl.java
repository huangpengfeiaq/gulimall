package com.hpf.gulimall.coupon.service.impl;

import com.hpf.common.to.MemberPrice;
import com.hpf.common.to.SkuReductionTO;
import com.hpf.gulimall.coupon.entity.MemberPriceEntity;
import com.hpf.gulimall.coupon.entity.SkuLadderEntity;
import com.hpf.gulimall.coupon.service.MemberPriceService;
import com.hpf.gulimall.coupon.service.SkuLadderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpf.common.utils.PageUtils;
import com.hpf.common.utils.Query;

import com.hpf.gulimall.coupon.dao.SkuFullReductionDao;
import com.hpf.gulimall.coupon.entity.SkuFullReductionEntity;
import com.hpf.gulimall.coupon.service.SkuFullReductionService;

import javax.annotation.Resource;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {
    @Resource
    SkuLadderService skuLadderService;
    @Resource
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTO skuReductionTO) {
        //1.保存满减打折，会员价
        //5.4 sku的优惠信、满减信息 gulimall_sms.sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuReductionTO, skuLadderEntity);
        if (skuReductionTO.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }
        //gulimall_sms.sms_sku_full_reduction
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTO, reductionEntity);
        if (reductionEntity.getFullPrice().compareTo(new BigDecimal(0)) > 0) {
            this.save(reductionEntity);
        }

        //gulimall_sms.sms_member_price
        List<MemberPrice> memberPrice = skuReductionTO.getMemberPrice();
        List<MemberPriceEntity> collect = memberPrice
                .stream()
                .map(item -> {
                    MemberPriceEntity priceEntity = new MemberPriceEntity();
                    priceEntity.setSkuId(skuReductionTO.getSkuId());
                    priceEntity.setMemberLevelId(item.getId());
                    priceEntity.setMemberLevelName(item.getName());
                    priceEntity.setMemberPrice(item.getPrice());
                    priceEntity.setAddOther(1);
                    return priceEntity;
                })
                .filter(item -> item.getMemberPrice().compareTo(new BigDecimal(0)) > 0)
                .collect(Collectors.toList());
        memberPriceService.saveBatch(collect);
    }

}