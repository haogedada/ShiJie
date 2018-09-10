package com.haoge.shijie.pojo.respModelBean;

import com.haoge.shijie.pojo.VideoBean;

public class VideoMsg {
    private int commentNum;
    private VideoBean videoBean;
    public VideoMsg(){

    }
    public VideoMsg( int commentNum, VideoBean videoBean) {
        this.commentNum = commentNum;
        this.videoBean = videoBean;
    }


    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public VideoBean getVideoBean() {
        return videoBean;
    }

    public void setVideoBean(VideoBean videoBean) {
        this.videoBean = videoBean;
    }
}
