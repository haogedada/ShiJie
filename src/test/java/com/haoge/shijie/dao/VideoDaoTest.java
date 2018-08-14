package com.haoge.shijie.dao;

import com.haoge.shijie.pojo.VideoBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoDaoTest {

    @Autowired
    private VideoDao videoDao;

    @Test
    public void queryVideoList() {
      int res = videoDao.queryCountByAll("热门");
        System.out.println(res);
    }
}