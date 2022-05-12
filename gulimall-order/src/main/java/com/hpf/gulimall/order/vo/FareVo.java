package com.hpf.gulimall.order.vo;

import com.hpf.common.vo.MemberAddressVo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FareVo {

    private MemberAddressVo address;

    private BigDecimal fare;

}
