package com.haoge.shijie.service;

public interface RegisterService {


    /**
     * 判断邮箱是否存在
     *
     * @param email
     * @return
     */
    boolean isExistenceEmail(String email);

    /**
     * 根据userName判断用户是否存在
     *
     * @param userName
     * @return
     */
    boolean isExistenceUser(String userName);


    /**
     * 获取验证码
     *
     * @param userName,email
     * @return
     */
    boolean getUserCode(String userName, String email);


}
