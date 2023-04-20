package com.memory.usercenter.mapper;

import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserMapperTest {
    @Resource
    UserService userService;

    @Test
    public void testAddUser() {
        User user = new User();
        user.setUserAccount("18535854762");
        user.setUsername("memory");
        user.setUserPassword("123456");
        user.setAvatarUrl("https://profile.csdnimg.cn/3/2/4/3_weixin_42302796");
        user.setGender("1");
        user.setPhone("18887877787");
        user.setEmail("3348407547@qq.com");

        boolean save = userService.save(user);
        Assertions.assertTrue(save);
        System.out.println(save);
    }

}