package com.hpf.gulimall.product.service.impl;

import com.hpf.gulimall.product.entity.AttrEntity;
import com.hpf.gulimall.product.service.AttrService;
import com.hpf.gulimall.product.vo.AttrGroupWithAttrsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpf.common.utils.PageUtils;
import com.hpf.common.utils.Query;

import com.hpf.gulimall.product.dao.AttrGroupDao;
import com.hpf.gulimall.product.entity.AttrGroupEntity;
import com.hpf.gulimall.product.service.AttrGroupService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Resource
    private AttrService attrService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        IPage<AttrGroupEntity> page;
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });
        }
        if (catelogId == 0) {
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper
            );
        } else {
            page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    queryWrapper.eq("catelog_id", catelogId)
            );
        }

        return new PageUtils(page);
    }

    @Override
    public List<AttrGroupWithAttrsVO> getAttrGroupWithAttrs(Long catelogId) {
        //1.查询分组信息
        List<AttrGroupEntity> list = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        //2.查询所有属性
        List<AttrGroupWithAttrsVO> collect = list.stream().map(attrGroup -> {
            AttrGroupWithAttrsVO attrGroupWithAttrsVO = new AttrGroupWithAttrsVO();
            BeanUtils.copyProperties(attrGroup, attrGroupWithAttrsVO);
            List<AttrEntity> attrs = attrService.getRelationAttr(attrGroupWithAttrsVO.getAttrGroupId());
            attrGroupWithAttrsVO.setAttrs(attrs);
            return attrGroupWithAttrsVO;
        }).collect(Collectors.toList());
        return collect;
    }

}