package com.hpf.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hpf.gulimall.product.entity.AttrEntity;
import com.hpf.gulimall.product.service.AttrAttrgroupRelationService;
import com.hpf.gulimall.product.service.AttrService;
import com.hpf.gulimall.product.service.CategoryService;
import com.hpf.gulimall.product.vo.AttrGroupRelationVO;
import com.hpf.gulimall.product.vo.AttrGroupWithAttrsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hpf.gulimall.product.entity.AttrGroupEntity;
import com.hpf.gulimall.product.service.AttrGroupService;
import com.hpf.common.utils.PageUtils;
import com.hpf.common.utils.R;

import javax.annotation.Resource;


/**
 * 属性分组
 *
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-24 21:22:18
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private AttrService attrService;
    @Resource
    private AttrAttrgroupRelationService relationService;

    /**
     * 添加属性与分组关联关系
     */
    @PostMapping("/attr/relation")
    public R addRelation(@RequestBody List<AttrGroupRelationVO> vos) {
        relationService.saveBatch(vos);
        return R.ok();
    }

    /**
     * 获取分类下所有分组&关联属性
     */
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrsVO> vos = attrGroupService.getAttrGroupWithAttrs(catelogId);
        return R.ok().put("data", vos);
    }

    /**
     * 获取指定分组关联的所有属性
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable Long attrgroupId) {
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data", entities);
    }

    /**
     * 获取属性分组没有关联的其他属性
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable Long attrgroupId,
                            @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.getNoRelationAttr(params, attrgroupId);
        return R.ok().put("page", page);
    }

    /**
     * 删除属性与分组的关联关系
     */
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrGroupRelationVO> vos) {
        attrService.deleteRelation(vos);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params, @PathVariable("catelogId") Long catelogId) {
        PageUtils page = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", page);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId) {
        AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long[] catelogPath = categoryService.findCatelogPath(attrGroup.getCatelogId());
        attrGroup.setCatelogPath(catelogPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds) {
        attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
