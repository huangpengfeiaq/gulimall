package com.hpf.gulimall.product;

import com.hpf.gulimall.product.entity.BrandEntity;
import com.hpf.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(1L);
        brandEntity.setDescript("华为");
        brandEntity.setName("huawei");
//        brandService.save(brandEntity);
        brandService.updateById(brandEntity);
        System.out.println("success");
    }

}
