package com.haoge.shijie.dto;

import com.haoge.shijie.pojo.AuxiliaryUserBean;


public class UserDTO {
    private Integer userId;
    private String userName, userNickname, userSex, userBirthday,
            bardianSign, headimgUrl, userEmail;
    private AuxiliaryUserBean auxiliaryUserBean;


    public UserDTO() {
    }

    public UserDTO(String userName, String userNickname,
                    String userSex, String userBirthdy, String bardianSign, String headimgUrl,
                    String userEmail) {

        this.userName = userName;
        this.userNickname = userNickname;
        this.userSex = userSex;
        this.userBirthday = userBirthdy;
        this.bardianSign = bardianSign;
        this.headimgUrl = headimgUrl;
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


}
