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
public class CollectionDaoTest {

    @Autowired
    private CollectionDao collectionDao;

    @Test
    public void queryCollectionList() {
        List<VideoBean> collectionBeanList = collectionDao.queryCollectionByUid(1);
        for (VideoBean collectionBean : collectionBeanList) {
            //System.out.println("发布视频者id"+collectionBean.getVideoBean().getUserId()+"收藏者:"+collectionBean.getUserId());
        }
    }

    @Test
    public void queryCollectionByUid() {
    }

    @Test
    public void queryCollByUidAndVid() {
    }

    @Test
    public void queryCollectionById() {
    }

    @Test
    public void insertCollection() {
    }

    @Test
    public void updateCollection() {
    }

    @Test
    public void deleteUserCollection() {
    }
}