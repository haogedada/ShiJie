package com.haoge.shijie.dao;

import com.google.gson.Gson;
import com.haoge.shijie.pojo.CommentatorBean;
import com.haoge.shijie.pojo.respModelBean.CommentList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentatorDaoTest {

    @Autowired
    private CommentatorDao commentatorDao;

    @Test
    public void queryCommByVdoIds() {
        List<CommentatorBean> commentatorBeans = commentatorDao.queryCommByVdoIds(2);
        for (CommentatorBean c :
                commentatorBeans) {
            System.out.println(c.getTxtContext());
            System.out.println(c.getUserBean().getUserPassword());
        }
    }
    @Test
    public void queryByVdoIdAndTuds() {

 }

}