package com.etc.Service;

import com.etc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;
    @Test
    public void registerTest(){
        String name="18020692796";
        String password="123456";
        userService.register(name,password);
    }
}
