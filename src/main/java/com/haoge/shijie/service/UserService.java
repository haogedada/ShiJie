package com.haoge.shijie.service;

import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.pojo.VideoBean;
import com.haoge.shijie.pojo.respModelBean.UserHomeBean;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    /**
     * 查询所有区域
     * @return
     */
    List<UserBean> findUsers();

    /**
     * 根据id查询区域
     * @param userId
     * @return
     */
    UserBean findUserById(Integer userId);

    /**
     * 根据id查询关联区域
     * @param userId
     * @return
     */
    UserBean findUserAndAuxById(Integer userId);

    /**
     * 根据id查询关联区域
     * @param userId
     * @return
     */
    UserBean findUserAndCollById(Integer userId);

    /**
     * 根据id查询关联区域
     * @param userId
     * @return
     */
    UserBean findUserAndFriendById(Integer userId,String friendType);

    /**
     * 根据id查询关联视频表区域
     * @param userId
     * @return
     */
    UserBean findUserAndVdoById(Integer userId);



    /**
     * 根据token查询区域
     * @param token
     * @return
     */
    UserBean findUserByToken(String token);
    /**
     * 根据token查询区域
     * @param token
     * @return
     */
    UserHomeBean goUserHomeByToken(String token);


    /**
     * 新增区域信息
     * @param user
     * @return
     */
    boolean addUser(UserBean user);

    /**
     * 新增区域信息
     * @param userName,userPassword,email
     * @return
     */
    boolean addUser(String userName, String userPassword, String email);

    /**
     * 修改区域信息
     * @param user
     * @return
     */
    boolean modifyUser(UserBean user);

    /**
     * 修改区域信息
     * @param
     * @return
     */
    String modifyUser(String nickName, String sex,String birthday,String sign,String token,String fileType);
    /**
     * 根据id删除区域
     * @param userID
     * @return
     */
    boolean delUser(Integer userID);

    /**
     * 根据token查询关注列表
     * @param token
     * @return
     */
    List<UserBean> goFriendList(String token,String friendType);

    /**
     * 根据token查询收藏列表
     * @param token
     * @return
     */
    List<VideoBean> goCollectionList(String token);
    /**
     * 根据userId获取用户首页信息
     * @param userId
     * @return
     */
    UserHomeBean goUserHomeByUid(Integer userId);

    /**
     * 判断邮箱是否存在
     * @param email
     * @return
     */
    boolean isExistenceEmail(String email);
    /**
     * 根据userName判断用户是否存在
     * @param userName
     * @return
     */
    boolean isExistenceUser(String userName);

    /**
     * 插入视频记录
     * @param title,content,token
     * @return
     */
    String [] addVideo(String title, String content, String token,MultipartFile[] file);
    /**
     * 获取验证码
     * @param userName,email
     * @return
     */
    boolean getUserCode(String userName, String email);

    /**
     * 修改密码
     * @param userName,code,password
     * @return
     */
    boolean modifyPassword(String userName, String code, String password);

    /**
     * 上传文件
     * @param file
     * @param filePath
     * @param fileName
     * @return
     */
    boolean upLoadFile(MultipartFile[] file, String [] filePath, String [] fileName) throws Exception;
}
