package com.haoge.shijie.pojo;

public class AuxiliaryUserBean {
     private Integer Id,userId;
     private String userRole,userCode,userPermission,userToken,code;
     public AuxiliaryUserBean(){

     }

    public AuxiliaryUserBean(Integer userId, String userRole, String userCode, String userPermission, String userToken,String code) {
        this.userId = userId;
        this.userRole = userRole;
        this.userCode = userCode;
        this.userPermission = userPermission;
        this.userToken = userToken;
        this.code = code;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(String userPermission) {
        this.userPermission = userPermission;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
