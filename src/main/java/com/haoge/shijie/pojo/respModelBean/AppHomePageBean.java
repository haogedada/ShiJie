package com.haoge.shijie.pojo.respModelBean;

import java.util.List;

public class AppHomePageBean {

    private Object homePageHead;
    private List<TypeListBean> videoTypeList;
    private Object tail;

    public AppHomePageBean() {

    }

    public Object getHomePageHead() {
        return homePageHead;
    }

    public void setHomePageHead(Object homePageHead) {
        this.homePageHead = homePageHead;
    }

    public List<TypeListBean> getVideoTypeList() {
        return videoTypeList;
    }

    public void setVideoTypeList(List<TypeListBean> videoTypeList) {
        this.videoTypeList = videoTypeList;
    }

    public Object getTail() {
        return tail;
    }

    public void setTail(Object tail) {
        this.tail = tail;
    }
}
