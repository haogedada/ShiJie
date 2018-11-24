package com.haoge.shijie.service;

import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.pojo.VideoBean;
import com.haoge.shijie.pojo.respModelBean.UserHomeBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    /**
     * 查询所有区域
     *
     * @return
     */
    List<UserBean> findUsers();

    /**
     * 根据id查询区域
     *
     * @param userId
     * @return
     */
    UserBean findUserById(int userId);

    /**
     * 根据id查询关联区域
     *
     * @param userId
     * @return
     */
    UserBean findUserAndAuxById(int userId);

    /**
     * 根据token查询区域
     *
     * @param token
     * @return
     */
    UserBean findUserByToken(String token);

    /**
     * 根据token查询区域
     *
     * @param token
     * @return
     */
    UserHomeBean goUserHomeByToken(String token);

    /**
     * 新增区域信息
     *
     * @param userName,userPassword,email
     * @return
     */
    boolean addUser(String userName, String userPassword, String email);

    /**
     * 修改区域信息
     *
     * @param
     * @return
     */
    boolean modifyUser(UserBean userBean, String token, MultipartFile file, String filePath);

    /**
     * 根据id删除区域
     *
     * @param userId
     * @return
     */
    boolean delUser(int userId);

    /**
     * 根据token查询关注列表
     *
     * @param token
     * @return
     */
    List<UserBean> goFriendList(String token, String friendType);

    /**
     * 根据token查询收藏列表
     *
     * @param token
     * @return
     */
    List<VideoBean> goCollectionList(String token);

    /**
     * 根据userId获取用户首页信息
     *
     * @param userId
     * @return
     */
    UserHomeBean goUserHomeByUid(int userId);

    /**
     * 修改密码
     *
     * @param userName,code,password
     * @return
     */
    boolean modifyPassword(String userName, String code, String password);


    /**
     * 添加好友
     *
     * @param token
     * @param friendId
     * @return
     */
    boolean addFollow(String token, int friendId);

    /**
     * 取消关注
     * @param token
     * @param friend
     * @return
     */
    boolean cancelFollow(String token,int friend);

    /**
     * 用户收藏视频
     * @param token
     * @param videoId
     * @return
     */
    boolean collectUserVideo(String token, int videoId);

    /**
     * 用户取消收藏
     * @param token
     * @param videoId
     * @return
     */
    boolean cancelCollectUserVideo(String token, int videoId);
}
