package com.memory.usercenter.service.impl;

import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@SpringBootTest
public class UserServiceImplTest {
    @Resource
    private UserService userService;

    @Test
    public void searchUserByTags() {
        List<String> tagNameList = Arrays.asList("java", "python");
        List<User> userList = userService.searchUserByTags(tagNameList);
        Assertions.assertNotNull(userList);
    }

    @Test
    public void printUUID() {
        for (int i = 0; i < 10; i++) {
            System.out.println(UUID.randomUUID());
            System.out.println((UUID.randomUUID() + ""));
            System.out.println((UUID.randomUUID() + "").substring(0, 8));
            System.out.println(UUID.randomUUID().getClass());
        }
    }

}