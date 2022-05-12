package com.hpf.common.vo;

import com.hpf.common.vo.OrderItemVo;
import lombok.Data;

import java.util.List;

@Data
public class WareSkuLockVo {
    private String orderSn;
    /**
     * 需要锁住的所有库存信息
     **/
    private List<OrderItemVo> locks;
}
