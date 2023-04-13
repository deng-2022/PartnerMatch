package com.memory.usercenter.once;

import com.memory.usercenter.model.User;
import com.memory.usercenter.service.UserService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * @author 邓哈哈
 * 2023/4/8 22:17
 * Function:
 * Version 1.0
 */
//@Component
public class InsertUser {
    @Resource
    private UserService userService;

    // 项目启动后, 每隔5秒就执行一次该方法
    @Scheduled(fixedDelay = 5000)
//    @Scheduled(initialDelay = 5000, fixedDelay = Long.MAX_VALUE)
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        System.out.println("go go go go");
        // 计时开始
        stopWatch.start();
        // 插入数据条数
        final int INSERT_NUM = 1000;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUserAccount("memory");
            user.setUsername("邓哈哈");
            user.setUserPassword("12345678");
            user.setAvatarUrl("");
            user.setGender("");
            user.setPhone("18889889898");
            user.setEmail("3348407547@qq.com");
            user.setUserStatus(0);
            user.setUserRole(1);
            user.setPlanetCode("17625");
            user.setTags("");
            // 插入数据
            userService.save(user);
        }
        // 计时结束
        stopWatch.stop();
        // 计算整个插入过程耗费的时间
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
