package com.haoge.shijie.pojo;

import java.sql.Timestamp;

public class CommentatorBean {

    private Integer txtId,userId,toUserId,toVideoId,
            commentatorTipNum,commentatorTrampleNum;
    private String txtContext;
    private Timestamp txtCreatTime;


    public CommentatorBean(){

    }

    public CommentatorBean(Integer userId, Integer toUserId, Integer toVideoId,
                           Integer commentatorTipNum, Integer commentatorTrampleNum,
                           String txtContext, Timestamp txtCreatTime) {
        this.userId = userId;
        this.toUserId = toUserId;
        this.toVideoId = toVideoId;
        this.commentatorTipNum = commentatorTipNum;
        this.commentatorTrampleNum = commentatorTrampleNum;
        this.txtContext = txtContext;
        this.txtCreatTime = txtCreatTime;
    }

    public CommentatorBean(Integer txtId, Integer userId, Integer toUserId, Integer toVideoId,
                           Integer commentatorTipNum, Integer commentatorTrampleNum,
                           String txtContext, Timestamp txtCreatTime) {
        this.txtId = txtId;
        this.userId = userId;
        this.toUserId = toUserId;
        this.toVideoId = toVideoId;
        this.commentatorTipNum = commentatorTipNum;
        this.commentatorTrampleNum = commentatorTrampleNum;
        this.txtContext = txtContext;
        this.txtCreatTime = txtCreatTime;
    }

    public Integer getTxtId() {
        return txtId;
    }

    public void setTxtId(Integer txtId) {
        this.txtId = txtId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getToVideoId() {
        return toVideoId;
    }

    public void setToVideoId(Integer toVideoId) {
        this.toVideoId = toVideoId;
    }

    public Integer getCommentatorTipNum() {
        return commentatorTipNum;
    }

    public void setCommentatorTipNum(Integer commentatorTipNum) {
        this.commentatorTipNum = commentatorTipNum;
    }

    public Integer getCommentatorTrampleNum() {
        return commentatorTrampleNum;
    }

    public void setCommentatorTrampleNum(Integer commentatorTrampleNum) {
        this.commentatorTrampleNum = commentatorTrampleNum;
    }

    public String getTxtContext() {
        return txtContext;
    }

    public void setTxtContext(String txtContext) {
        this.txtContext = txtContext;
    }

    public Timestamp getTxtCreatTime() {
        return txtCreatTime;
    }

    public void setTxtCreatTime(Timestamp txtCreatTime) {
        this.txtCreatTime = txtCreatTime;
    }
}
