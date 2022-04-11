package com.hpf.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.hpf.gulimall.product.entity.CategoryBrandRelationEntity;
import com.hpf.gulimall.product.service.CategoryBrandRelationService;
import com.hpf.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hpf.common.utils.PageUtils;
import com.hpf.common.utils.Query;

import com.hpf.gulimall.product.dao.CategoryDao;
import com.hpf.gulimall.product.entity.CategoryEntity;
import com.hpf.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1.查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2.组装成父子的树形结构

        //2.1).找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream()
                .filter(categoryEntity -> categoryEntity.getParentCid() == 0)
                .peek(menu -> menu.setChildren(getChildren(menu, entities)))
//                .sorted(Comparator.comparingInt(CategoryEntity::getSort))
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
        return level1Menus;
    }

    /**
     * 递归查找所有菜单的子菜单
     */
    private List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream()
                .filter(categoryEntity -> Objects.equals(categoryEntity.getParentCid(), root.getCatId()))
                //1.找到子菜单
                .peek(categoryEntity -> categoryEntity.setChildren(getChildren(categoryEntity, all)))
                //2.菜单的排序
                .sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort())))
                .collect(Collectors.toList());
        return children;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前删除的菜单，是否被别的地方引用
        List<CategoryBrandRelationEntity> categoryBrandRelation =
                categoryBrandRelationService.list(new QueryWrapper<CategoryBrandRelationEntity>().in("catelog_id", asList));

        if (categoryBrandRelation.size() == 0) {
            //逻辑删除
            baseMapper.deleteBatchIds(asList);
        } else {
            throw new RuntimeException("该菜单下面还有属性，无法删除!");
        }
    }

    //[2,34,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        //递归查询是否还有父节点
        List<Long> paths = findParentPath(catelogId, new ArrayList<>());
        //进行一个逆序排列
        Collections.reverse(paths);

        return paths.toArray(new Long[0]);
    }

    //[225,34,2]
    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1.收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = baseMapper.selectById(catelogId);
        if (byId.getParentCid() != 0) {
            this.findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    /**
     * 级联更新所有关联的数据
     * <p>
     * >@CacheEvict:失效模式
     * >@CachePut:双写模式，需要有返回值
     * 1、同时进行多种缓存操作：@Caching
     * 2、指定删除某个分区下的所有数据 @CacheEvict(value = "category",allEntries = true)
     * 3、存储同一类型的数据，都可以指定为同一分区
     */
    // @Caching(evict = {
    //         @CacheEvict(value = "category",key = "'getLevel1'"),
    //         @CacheEvict(value = "category",key = "'getCatalogJson'")
    // })
    @CacheEvict(value = "category", allEntries = true)       //删除某个分区下的所有数据
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateCascade(CategoryEntity category) {

        RReadWriteLock readWriteLock = redissonClient.getReadWriteLock("catalogJson-lock");
        //创建写锁
        RLock rLock = readWriteLock.writeLock();

        try {
            rLock.lock();
            this.baseMapper.updateById(category);
            categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }

        //同时修改缓存中的数据
        //删除缓存,等待下一次主动查询进行更新
    }

    /**
     * 1.每一个需要缓存的数据我们都来指定要放到那个名字的缓存。【缓存的分区(按照业务类型分)】
     * 2.@Cacheable 代表当前方法的结果需要缓存，如果缓存中有，方法都不用调用，如果缓存中没有，会调用方法。最后将方法的结果放入缓存
     * 3.默认行为
     * 1）、如果缓存中有，方法不再调用
     * 2）、key是默认生成的:缓存的名字::SimpleKey::[](自动生成key值)
     * 3）、缓存的value值，默认使用jdk序列化机制，将序列化的数据存到redis中
     * 4）、默认时间是 -1：
     * <p>
     * 自定义操作：key的生成
     * 1）、指定生成缓存的key：key属性指定，接收一个SpEl
     * 2）、指定缓存的数据的存活时间:配置文档中修改存活时间
     * 3）、将数据保存为json格式
     * <p>
     * <p>
     * 4、Spring-Cache的不足之处：
     * 1）、读模式
     * 缓存穿透：查询一个null数据。解决方案：缓存空数据
     * 缓存击穿：大量并发进来同时查询一个正好过期的数据。解决方案：加锁 ? 默认是无加锁的;使用sync = true来解决击穿问题
     * 缓存雪崩：大量的key同时过期。解决：加随机时间。加上过期时间
     * 2)、写模式：（缓存与数据库一致）
     * 1）、读写加锁。
     * 2）、引入Canal,感知到MySQL的更新去更新Redis
     * 3）、读多写多，直接去数据库查询就行
     * <p>
     * 总结：
     * 常规数据（读多写少，即时性，一致性要求不高的数据，完全可以使用Spring-Cache）：写模式(只要缓存的数据有过期时间就足够了)
     * 特殊数据：特殊设计
     * <p>
     * 原理：
     * CacheManager(RedisCacheManager)->Cache(RedisCache)->Cache负责缓存的读写
     */
    @Cacheable(value = {"category"}, key = "#root.method.name", sync = true)
    @Override
    public List<CategoryEntity> getLevel1() {
        System.out.println("getLevel1........");
        long l = System.currentTimeMillis();
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        System.out.println("消耗时间：" + (System.currentTimeMillis() - l));
        return categoryEntities;
    }

    @Cacheable(value = "category", key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        return getDataFromDB();
    }

    private Map<String, List<Catelog2Vo>> getDataFromDB() {
        System.out.println("查询了数据库...");
        //1.将数据库中的多次查询变为一次,存至缓存selectList,需要的数据从list取出,避免频繁的数据库交互2
        List<CategoryEntity> selectList = baseMapper.selectList(null);
        //2.查出所有1级分类
        List<CategoryEntity> level1 = getParent_cid(selectList, 0L);
        //3.封装数据
        Map<String, List<Catelog2Vo>> parent_cid = level1.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1.查出1级分类中所有2级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            //2.封装上面的结果
            List<Catelog2Vo> catelog2Vos = null;
            if (categoryEntities != null) {
                catelog2Vos = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //查询当前2级分类的3级分类
                    List<CategoryEntity> level3 = getParent_cid(selectList, l2.getCatId());
                    if (level3 != null) {
                        List<Catelog2Vo.Catelog3Vo> collect = level3.stream().map(l3 -> {
                            //封装指定格式
                            return new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());
            }
            return catelog2Vos;
        }));
        return parent_cid;
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        return selectList.stream().filter(item -> Objects.equals(item.getParentCid(), parent_cid)).collect(Collectors.toList());
    }

///////////////////////////////////以下为各种手动添加的缓存加锁版本///////////////////////////////////////////////////////////////

    /**
     * 没有锁
     *
     * @return 分类数据集
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDb() {
        return getDataFromDBLock();
    }

    /**
     * 本地进程锁
     *
     * @return 分类数据集
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithLocalLock() {
        //0.从缓存中取出数据
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        //0.x.若缓存中有数据
        if (!StringUtils.isEmpty(catalogJSON)) {
            return JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
        }
        //0.y.若缓存中没有数据，
        synchronized (this) {
            return getDataFromDBLock();
        }
    }

    /**
     * 分布式程锁Redis
     *
     * @return 分类数据集
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedisLock() {
        //0.从缓存中取出数据
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        //0.x.若缓存中有数据
        if (!StringUtils.isEmpty(catalogJSON)) {
            return JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
        }
        //0.y.若缓存中没有数据，

        //1.占分布式锁,去redis占坑。同时设置锁过期时间,必须和加锁同步，原子操作
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(lock)) {
            //加锁成功,执行业务
            Map<String, List<Catelog2Vo>> dataFromDB;
            try {
                dataFromDB = getDataFromDBLock();
            } finally {
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1])else return 0 end";
                //删除锁,Lua脚本，原子操作
                Long lock1 = redisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList("lock"), uuid);
            }
            return dataFromDB;
        } else {
            //加锁失败,休眠2秒,重试
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return getCatalogJsonFromDbWithRedisLock();//自旋
        }
    }

    /**
     * 分布式锁Redisson
     * 缓存里的数据如何和数据库的数据保持一致？？
     * 缓存数据一致性
     * 1)、双写模式
     * 2)、失效模式
     *
     * @return 分类数据集
     */
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDbWithRedissonLock() {
        //0.从缓存中取出数据
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        //0.x.若缓存中有数据
        if (!StringUtils.isEmpty(catalogJSON)) {
            return JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
        }
        //0.y.若缓存中没有数据，

        //1、占分布式锁。去redis占坑
        //（锁的粒度，越细越快:具体缓存的是某个数据，11号商品） product-11-lock
        //RLock catalogJsonLock = redissonClient.getLock("catalogJson-lock");
        //创建读锁
        RLock lock = redissonClient.getLock("catalogJson-lock");
        lock.lock();
        //加锁成功,执行业务
        Map<String, List<Catelog2Vo>> dataFromDB;
        try {
            dataFromDB = getDataFromDBLock();
        } finally {
            lock.unlock();
        }
        return dataFromDB;
    }


    /**
     * 1.空结果缓存，解决缓存穿透
     * 2.设置过期时间（加随机值），解决缓存雪崩
     * 3.加锁，解决缓存击穿
     *
     * @return 包含redis缓存的分类数据
     */
    private Map<String, List<Catelog2Vo>> getDataFromDBLock() {
        //从缓存中取出的json数据要逆转为能用的对象类型,【序列化与发序列化】

        //1.加入缓存逻辑，缓存中的数据是json字符串
        //JSON跨语言,跨平台兼容
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");

        //2.x.缓存中有，转为我们指定的对象
        if (!StringUtils.isEmpty(catalogJSON)) {
            return JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
        }

        //2.y.缓存中没有，从数据库查询并封装分类数据
        Map<String, List<Catelog2Vo>> parent_cid = getDataFromDB();

        //3.将查到的数据放入缓存，将查出的对象转为json放在缓存中
        redisTemplate.opsForValue().set("catalogJSON", JSON.toJSONString(parent_cid), 1, TimeUnit.DAYS);
        return parent_cid;
    }
}