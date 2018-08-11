package com.haoge.shijie.dao;

import com.haoge.shijie.pojo.UserBean;

public interface LoginDao {
    /**
     * 根据用户账号密码查询区域
     * @param userName,passWord
     * @return
     */
    UserBean userLoginQuery(String userName, String passWord);

    /**
     * 根据用户账号查询区域
     * @param userName,passWord
     * @return
     */
    UserBean userLoginQueryByName(String userName);
}
