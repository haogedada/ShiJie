package com.haoge.shijie.dao;

import com.haoge.shijie.pojo.UserBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    /**
     * 查询所有区域
     *
     * @return
     */
    List<UserBean> queryUserList();

    /**
     * 根据id查询区域
     *
     * @param userId
     * @return
     */

    UserBean queryUserById(Integer userId);

    /**
     * 根据id查询关联AuxiliaryUser表信息区域
     *
     * @param userId
     * @return
     */
    UserBean queryUserAndAuxById(Integer userId);


    /**
     * 根据id查询关联Friend表信息区域
     *
     * @param userId
     * @return
     */
    UserBean queryUserAndFriendById(@Param("userId") Integer userId, @Param("friendType") String friendType);

    /**
     * 根据id查询关联Collection表信息区域
     *
     * @param userId
     * @return
     */
    UserBean queryUserAndCollById(Integer userId);

    /**
     * 根据用户名查询区域
     *
     * @param userName
     * @return
     */
    UserBean queryUserByName(String userName);

    /**
     * 根据邮箱查询区域
     *
     * @param userEmail
     * @return
     */
    int queryUserByEmail(String userEmail);

    /**
     * 新增区域信息
     *
     * @param user
     * @return
     */
    int insertUser(UserBean user);

    /**
     * 修改区域信息
     *
     * @param user
     * @return
     */
    int updateUser(UserBean user);

    /**
     * 根据id删除区域
     *
     * @param userId
     * @return
     */
    int deleteUser(Integer userId);
}
