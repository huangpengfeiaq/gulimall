package com.hpf.gulimall.product;

import com.hpf.gulimall.product.entity.BrandEntity;
import com.hpf.gulimall.product.service.BrandService;
import com.hpf.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    public void testStringRedisTemplate() {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        //保存
        opsForValue.set("hello", "world_" + UUID.randomUUID());
        //查询
        String hello = opsForValue.get("hello");
        System.out.println("之前保存的数据是：" + hello);
    }

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }

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
