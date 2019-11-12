package com.cp.shiro_springboot;

import com.cp.shiro_springboot.pojo.User;
import com.cp.shiro_springboot.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShiroSpringbootApplicationTests {

    @Autowired
    UserServiceImpl userService;

    @Test
    void contextLoads() {
        User user = userService.queryUserByName("cp");
        System.out.println(user);
    }

}
