package com.hpf.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hpf.common.utils.PageUtils;
import com.hpf.gulimall.product.entity.AttrEntity;
import com.hpf.gulimall.product.vo.AttrGroupRelationVO;
import com.hpf.gulimall.product.vo.AttrGroupWithAttrsVO;
import com.hpf.gulimall.product.vo.AttrRespVO;
import com.hpf.gulimall.product.vo.AttrVO;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-24 20:51:43
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVO attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVO getAttrInfo(Long attrId);

    void updateByIdAttr(AttrVO attrVO);

    /**
     * 根据分组id查找关联的所有基本属性
     * @param attrgroupId
     * @return
     */
    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(List<AttrGroupRelationVO> vos);

    /**
     * 获取属性分组没有关联的其他属性
     * @param params {
     *    page: 1,//当前页码
     *    limit: 10,//每页记录数
     *    sidx: 'id',//排序字段
     *    order: 'asc/desc',//排序方式
     *    key: '华为'//检索关键字
     * }
     * @param attrgroupId
     * @return
     */
    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

}

