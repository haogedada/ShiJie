package com.haoge.shijie.pojo.respModelBean;


import com.haoge.shijie.pojo.VideoBean;

import java.util.List;

public class UserHomeBean {
    private String nickName,heardUrl;
    private int followNum,fansNum,videoNum;
    private List<VideoBean> videos;

    public UserHomeBean(){

    }
    public UserHomeBean(String nickName, String heardUrl, int followNum, int fansNum,
                        int videoNum, List<VideoBean> videos) {
        this.nickName = nickName;
        this.heardUrl = heardUrl;
        this.followNum = followNum;
        this.fansNum = fansNum;
        this.videoNum = videoNum;
        this.videos = videos;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeardUrl() {
        return heardUrl;
    }

    public void setHeardUrl(String heardUrl) {
        this.heardUrl = heardUrl;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public int getVideoNum() {
        return videoNum;
    }

    public void setVideoNum(int videoNum) {
        this.videoNum = videoNum;
    }

    public List<VideoBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideoBean> videos) {
        this.videos = videos;
    }
}
