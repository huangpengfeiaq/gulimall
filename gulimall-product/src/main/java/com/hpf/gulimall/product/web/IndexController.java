package com.hpf.gulimall.product.web;

import com.hpf.gulimall.product.entity.CategoryEntity;
import com.hpf.gulimall.product.service.CategoryService;
import com.hpf.gulimall.product.vo.Catelog2Vo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;

//    @Resource
//    private RedissonClient redisson;
//
//    @Resource
//    private StringRedisTemplate redisTemplate;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //查出所有一级分类
        List<CategoryEntity> categoryList = categoryService.getLevel1();
        model.addAttribute("categorys", categoryList);
        return "index";
    }


    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        Map<String, List<Catelog2Vo>> catalogJson = categoryService.getCatalogJson();
        return catalogJson;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
//        //只有锁名字一样,就是同一把锁
//        RLock lock = redisson.getLock("my-lock");
//        //加锁,阻塞式等待
//        lock.lock();
//        try {
//            System.out.println("加锁测试,执行业务..." + Thread.currentThread().getId());
//            Thread.sleep(10000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            //解锁
//            System.out.println("释放锁" + Thread.currentThread().getId());
//            lock.unlock();
//        }
        return "hello";
    }

//
//    @GetMapping("/write")
//    @ResponseBody
//    public String writeValue() {
//        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
//        String s = "";
//        //加写锁
//        RLock rLock = lock.writeLock();
//        try {
//            rLock.lock();
//            s = UUID.randomUUID().toString();
//            redisTemplate.opsForValue().set("writeValue", s);
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            rLock.unlock();
//        }
//        return s;
//    }
//
//    @GetMapping("/read")
//    @ResponseBody
//    public String redValue() {
//        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
//        String s = "";
//        //加读锁
//        RLock rLock = lock.readLock();
//        rLock.lock();
//        try {
//            s = redisTemplate.opsForValue().get("writeValue");
//            Thread.sleep(1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            rLock.unlock();
//        }
//        return  s;
//    }
}
