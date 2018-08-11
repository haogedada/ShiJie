package com.haoge.shijie.pojo;

import java.sql.Timestamp;
import java.util.List;

public class UserFriendsBean {
    private Integer id,userId,friendId;
    private Timestamp creatTime;
    private String friendType;
    private UserBean user;
    public UserFriendsBean(){

    }
    public UserFriendsBean(Integer id, Integer userId,
                           Integer friendId, Timestamp creatTime, String friendType) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
        this.creatTime = creatTime;
        this.friendType = friendType;
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

    public Integer getFriendId() {
        return friendId;
    }

    public void setFriendId(Integer friend) {
        this.friendId = friend;
    }

    public Timestamp getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(Timestamp creatTime) {
        this.creatTime = creatTime;
    }

    public String getFriendType() {
        return friendType;
    }

    public void setFriendType(String friendType) {
        this.friendType = friendType;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        user.setUserPassword("");
        user.setUserEmail("");
        user.setRegisterTime(null);
        this.user = user;
    }
}
