package com.haoge.shijie.dao;

import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.pojo.UserFriendsBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserFriendsDao {

    /**
     * 根据userId,friendType查询到好友列表
     * @param userId,friendType
     * @return
     */
    List<UserBean> queryFriendByIdAndType(@Param("userId") Integer userId, @Param("friendType") String friendType);


    /**
     * 新增区域信息
     * @param userFriend
     * @return
     */
    int insertUserFriend(UserFriendsBean userFriend);

    /**
     * 修改区域信息
     * @param userFriend
     * @return
     */
    int updateUserFriend(UserFriendsBean userFriend);

    /**
     * 根据id删除区域
     * @param userId
     * @return
     */
    int deleteUserFriend(@Param("userId") Integer userId,@Param("friendId") Integer friendId);
}
