package com.haoge.shijie.pojo;

import java.sql.Timestamp;

public class CommentatorBean {
    private Integer txtId, userId, toTxtId, toVideoId,
            commentatorTipNum, commentatorTrampleNum;
    private String txtContext;
    private Timestamp txtCreatTime;
    private UserBean userBean;

    public CommentatorBean() {

    }

    public CommentatorBean(Integer userId, Integer toTxtId, Integer toVideoId,
                           Integer commentatorTipNum, Integer commentatorTrampleNum,
                           String txtContext, Timestamp txtCreatTime) {
        this.userId = userId;
        this.toTxtId = toTxtId;
        this.toVideoId = toVideoId;
        this.commentatorTipNum = commentatorTipNum;
        this.commentatorTrampleNum = commentatorTrampleNum;
        this.txtContext = txtContext;
        this.txtCreatTime = txtCreatTime;
    }

    public CommentatorBean(Integer txtId, Integer userId, Integer toTxtId, Integer toVideoId,
                           Integer commentatorTipNum, Integer commentatorTrampleNum,
                           String txtContext, Timestamp txtCreatTime) {
        this.txtId = txtId;
        this.userId = userId;
        this.toTxtId = toTxtId;
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

    public Integer getToTxtId() {
        return toTxtId;
    }

    public void setToTxtId(Integer toTxtId) {
        this.toTxtId = toTxtId;
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

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        userBean.setUserPassword("");
        this.userBean = userBean;
    }
}
