package com.memory.usercenter;

import com.memory.usercenter.mapper.UserMapper;
import com.memory.usercenter.model.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

import org.junit.Assert;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

@SpringBootTest(classes = UserCenterApplication.class)
@RunWith(SpringRunner.class)
class UserCenterApplicationTests {
    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        final String SALT = "memory";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + 123456).getBytes());
        System.out.println(encryptPassword);
    }


    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        Assert.assertEquals(5, userList.size());
        userList.forEach(System.out::println);
    }

}
