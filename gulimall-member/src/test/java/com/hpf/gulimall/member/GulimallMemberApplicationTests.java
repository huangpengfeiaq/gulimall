package com.hpf.gulimall.member;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.Md5Crypt;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// @RunWith(SpringRunner.class)
// @SpringBootTest
public class GulimallMemberApplicationTests {

    @Test
    public void contextLoads() {

        String s = DigestUtils.md5Hex("123456");
        System.out.println(s);//e10adc3949ba59abbe56e057f20f883e
        System.out.println(Md5Crypt.md5Crypt("123456".getBytes()));//$1$S.DAoRdo$4lEfS3FWwfB09hOShmxnb.
        System.out.println(Md5Crypt.md5Crypt("123456".getBytes(),"$1$qqqqqqqq"));//$1$qqqqqqqq$AZofg3QwurbxV3KEOzwuI1

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode("123456");
        System.out.println(encode);
        boolean matches = bCryptPasswordEncoder.matches("123456", encode);

        System.out.println("==>" + matches);
    }

}