package com.memory.usercenter;

import com.memory.usercenter.model.User;
import com.memory.usercenter.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author 邓哈哈
 * 2023/4/8 22:41
 * Function:
 * Version 1.0
 */
@SpringBootTest
public class InsertUserTest {
    @Resource
    private UserService userService;

    public static final String SALT = "memory";

    /**
     * 插入数据
     */
    @Test
    public void doInsertUsers1() {
        StopWatch stopWatch = new StopWatch();
        System.out.println("go go go go");
        stopWatch.start();

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

            userService.save(user);
        }

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }


    /**
     * 批量插入数据
     */
    @Test
    public void doInsertUsers2() {
        StopWatch stopWatch = new StopWatch();
        System.out.println("go go go go");
        stopWatch.start();

        ArrayList<User> userList = new ArrayList<>();

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
            userList.add(user);
        }
        userService.saveBatch(userList, 100);

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    /**
     * 并发批量插入数据
     */
    @Test
    public void doConcurrencyInsertUsers() {
        // new一个StopWatch对象
        StopWatch stopWatch = new StopWatch();
        // 计时开始
        stopWatch.start();
        // 每条线程插入1000条
        int batchSize = 100;
        int j = 0;
        // 创建一个异步任务集合
        ArrayList<CompletableFuture<Void>> futureList = new ArrayList<>();
        // 开10条线程
        for (int i = 0; i < 10; i++) {
            // 每条线程下new一个userList
            ArrayList<User> userList = new ArrayList<>();
            while (true) {
                j++;
                User user = new User();
                user.setUserAccount("memory" + "_" + (UUID.randomUUID() + "").substring(0, 8));
//                user.setUsername("邓哈哈");
                user.setUsername("邓哇哇");
                String password = DigestUtils.md5DigestAsHex((SALT + 12345678).getBytes());
                user.setUserPassword(password);
                user.setAvatarUrl("https://fastly.jsdelivr.net/npm/@vant/assets/ipad.jpeg");
//                user.setGender("1");
                user.setGender("0");
                user.setPhone("18535854763");
                user.setEmail("3348407547@qq.com");
                user.setUserStatus(0);
                user.setUserRole(0);
//                user.setTags("[\"男\",\"Java\",\"Python\",\"在校本科\",\"开朗\",\"努力中\"]");
                user.setTags("[\"女\",\"Vue\",\"Python\",\"在校本科\",\"发呆\",\"emo中\"]");

                userList.add(user);
                // 当该线程插满1000条数据，便退出该线程循环
                if (j % batchSize == 0) {
                    break;
                }
            }
            // 异步条件下, 执行批量插入
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println("threadName: " + Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            });
            // 将该任务存储到异步任务集合当中
            futureList.add(future);
        }
        // 结束所有异步任务
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{})).join();
        // 计时结束
        stopWatch.stop();
        // 计算插入所用总时间
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
