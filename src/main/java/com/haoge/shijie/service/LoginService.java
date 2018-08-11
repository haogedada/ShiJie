package com.haoge.shijie.service;

import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.pojo.response.ResponseBean;

public interface LoginService {

    /**
     * 根据用户账号查询区域
     * @param userName
     * @return
     */
    ResponseBean findToken(String userName, String userPassword);
    /**
     * 根据用户账号密码查询区域
     * @param userName,passWord
     * @return
     */
    UserBean findUserByName(String userName);

}
