package com.hpf.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.hpf.gulimall.search.config.ElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {
    @Autowired
    private RestHighLevelClient client;

    /**
     * 测试存储到es
     */
    @Test
    public void indexData() throws Exception {
        IndexRequest indexRequest = new IndexRequest("users")
                .id("1");
//        indexRequest.source("userName", "ZhangSan", "age", "18", "gender", "男");
        User user = new User();
        user.setUserName("ZhangSan");
        user.setAge(18);
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);

        IndexResponse index = client.index(indexRequest, ElasticSearchConfig.COMMON_OPTIONS);

        System.out.println(index);
    }

    @Data
    class User {
        String userName;
        Integer age;
        String gender;
    }

    @Test
    public void contextLoads() {
        System.out.println(client);
    }
}
