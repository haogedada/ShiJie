package com.haoge.shijie.pojo;

import java.sql.Timestamp;
import java.util.List;

public class UserBean {
    private Integer userId;
    private String userName,userPassword,userNickname,userSex,userBirthday,
            bardianSign,headimgUrl,userEmail;
    private Timestamp registerTime;
    private AuxiliaryUserBean auxiliaryUserBean;
    private List<VideoBean> videoBeans;
    private List<UserFriendsBean> userFriends;
    private List<CollectionBean> collections;
    public UserBean(){
    }

    public UserBean( String userName, String userPassword, String userNickname,
                     String userSex, String userBirthdy, String bardianSign, String headimgUrl,
                     Timestamp registerTime, String userEmail) {

        this.userName = userName;
        this.userPassword = userPassword;
        this.userNickname = userNickname;
        this.userSex = userSex;
        this.userBirthday = userBirthdy;
        this.bardianSign = bardianSign;
        this.headimgUrl = headimgUrl;
        this.registerTime = registerTime;
        this.userEmail = userEmail;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthdy) {
        this.userBirthday = userBirthdy;
    }

    public String getBardianSign() {
        return bardianSign;
    }

    public void setBardianSign(String bardianSign) {
        this.bardianSign = bardianSign;
    }

    public String getHeadimgUrl() {
        return headimgUrl;
    }

    public void setHeadimgUrl(String headimgUrl) {
        this.headimgUrl = headimgUrl;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public AuxiliaryUserBean getAuxiliaryUserBean() {
        return auxiliaryUserBean;
    }

    public void setAuxiliaryUserBean(AuxiliaryUserBean auxiliaryUserBean) {
        this.auxiliaryUserBean = auxiliaryUserBean;
    }

    public List<VideoBean> getVideoBeans() {
        return videoBeans;
    }

    public void setVideoBeans(List<VideoBean> videoBeans) {
        this.videoBeans = videoBeans;
    }

    public List<UserFriendsBean> getUserFriends() {
        return userFriends;
    }

    public void setUserFriends(List<UserFriendsBean> userFriends) {
        this.userFriends = userFriends;
    }

    public List<CollectionBean> getCollections() {
        return collections;
    }

    public void setCollections(List<CollectionBean> collections) {
        this.collections = collections;
    }

}
