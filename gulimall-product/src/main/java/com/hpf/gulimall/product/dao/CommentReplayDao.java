package com.hpf.gulimall.product.dao;

import com.hpf.gulimall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author huangpengfei
 * @email 641655770@qq.com
 * @date 2021-11-24 20:51:43
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
