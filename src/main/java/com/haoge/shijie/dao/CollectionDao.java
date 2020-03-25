package com.haoge.shijie.dao;

import com.haoge.shijie.pojo.CollectionBean;
import com.haoge.shijie.pojo.VideoBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CollectionDao {

    /**
     * 查询所有收藏列表
     *
     * @param
     * @return
     */
    List<CollectionBean> queryCollectionList();

    /**
     * 根据userId查询到收藏列表
     *
     * @param userId,friendType
     * @return
     */
    List<VideoBean> queryCollectionByUid(Integer userId);

    /**
     * 根据id查询到收藏记录
     *
     * @param id
     * @return
     */
    CollectionBean queryCollectionById(Integer id);

    /**
     * 新增区域信息
     *
     * @param collection
     * @return
     */
    int insertCollection(CollectionBean collection);

    /**
     * 修改区域信息
     *
     * @param collection
     * @return
     */
    int updateCollection(CollectionBean collection);

    /**
     * 根据userId,videoId删除区域
     *
     * @param userId,videoId
     * @return
     */
    int deleteUserCollection(@Param("userId") Integer userId, @Param("videoId") Integer videoId);

}
