package com.haoge.shijie.dao;

import com.haoge.shijie.pojo.UserBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserFriendsDaoTest {

    @Autowired
    private UserFriendsDao userFriendsDao;

    @Test
    public void queryFriendByIdAndType() {
        List<UserBean> userFriendsBeans = userFriendsDao.queryFriendByIdAndType(1, "fans");
        for (UserBean u : userFriendsBeans) {
        }

    }
}