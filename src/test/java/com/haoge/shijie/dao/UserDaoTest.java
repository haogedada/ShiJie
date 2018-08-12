package com.haoge.shijie.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void queryUserById() {
        int i = userDao.queryUserByEmail("2205584662@qq.com");
        System.out.println(i);
    }
}