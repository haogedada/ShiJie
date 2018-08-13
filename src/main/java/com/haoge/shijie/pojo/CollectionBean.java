package com.haoge.shijie.pojo;

import java.sql.Timestamp;

public class CollectionBean {
    private Integer id, userId, videoId;
    private Timestamp creatTime;
    private UserBean userBean;
    private VideoBean videoBean;

    public CollectionBean() {

    }

    public CollectionBean(Integer id, Integer userId, Integer videoId, Timestamp creatTime) {
        this.id = id;
        this.userId = userId;
        this.videoId = videoId;
        this.creatTime = creatTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVideoId() {
        return videoId;
    }

    public void setVideoId(Integer videoId) {
        this.videoId = videoId;
    }

    public Timestamp getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Timestamp creatTime) {
        this.creatTime = creatTime;
    }

    public VideoBean getVideoBean() {
        return videoBean;
    }

    public void setVideoBean(VideoBean videoBean) {
        this.videoBean = videoBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}
