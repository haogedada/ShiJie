package com.haoge.shijie.service.impl;

import com.haoge.shijie.constant.Constants;
import com.haoge.shijie.dao.*;
import com.haoge.shijie.pojo.*;
import com.haoge.shijie.pojo.respModelBean.UserHomeBean;
import com.haoge.shijie.service.FileService;
import com.haoge.shijie.service.UserService;
import com.haoge.shijie.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.haoge.shijie.constant.Constants.mailType.ACTIVATION;
import static com.haoge.shijie.constant.Constants.pathType.HEADPATH;
import static com.haoge.shijie.constant.Constants.prefixType.HEADIMGPREFIX;
import static com.haoge.shijie.constant.Constants.roleType.USERROLE;
import static com.haoge.shijie.constant.Constants.urlType.HEADURL;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private UserFriendsDao userFriendsDao;
    @Autowired
    private CollectionDao collectionDao;
    @Autowired
    private AuxiliaryUserDao auxiliaryUserDao;
    @Autowired
    private FileService fileService;


    /**
     * 用户列表
     *
     * @return
     */
    @Override
    public List<UserBean> findUsers() {
        List<UserBean> userBeans = userDao.queryUserList();
        if (userBeans.size() > 0) {
            return userBeans;
        } else {
            throw new RuntimeException("用户列表为空");
        }
    }

    /**
     * 通过userId查找用户
     *
     * @param userId
     * @return userBean 用户信息
     */
    @Override
    @Cacheable(value = "userCache")
    public UserBean findUserById(int userId) {
        if (StrJudgeUtil.isCorrectInt(userId)) {
            UserBean userBean = userDao.queryUserById(userId);
            if (userBean == null || !StrJudgeUtil.isCorrectInt(userBean.getUserId())) {
                throw new RuntimeException("用户不存在");
            } else {
                return userBean;
            }
        } else {
            throw new RuntimeException("用户id不合法");
        }
    }

    /**
     * 通过用户id查询关联AuxiliaryUser表信息区域
     *
     * @param userId
     * @return
     */
    @Override
    @Cacheable(value = "userCache")
    public UserBean findUserAndAuxById(int userId) {
        if (StrJudgeUtil.isCorrectInt(userId)) {
            UserBean userBean = userDao.queryUserAndAuxById(userId);
            if (userBean != null && StrJudgeUtil.isCorrectStr(userBean.getUserName()) &&
                    userBean.getAuxiliaryUserBean() != null) {
                return userBean;
            } else {
                throw new RuntimeException("findUserAndAuxById错误");
            }
        } else {
            throw new RuntimeException("用户不存在");
        }

    }

    /**
     * 通过token查找用户
     *
     * @param token
     * @return
     */
    @Override
    @Cacheable(value = "userCache")
    public UserBean findUserByToken(String token) {
        String userName = JWTUtil.getUsername(token);
        UserBean userBean = userDao.queryUserByName(userName);
        userBean.setUserPassword("");
        if (userBean == null || !StrJudgeUtil.isCorrectInt(userBean.getUserId())) {
            throw new RuntimeException("用户不存在");
        } else {
            return userBean;
        }
    }

    /**
     * 通过token查找用户自己相关信息
     *
     * @param token
     * @return
     */
    @Override
    @Cacheable(value = "userAndVideo")
    public UserHomeBean goUserHomeByToken(String token) {
        UserHomeBean userHome = new UserHomeBean();
        UserBean userBean = this.findUserByToken(token);
        int fansNum = userFriendsDao.queryFriendByIdAndType(userBean.getUserId(), Constants.friendType.FANS.getName()).size();
        int followNum = userFriendsDao.queryFriendByIdAndType(userBean.getUserId(), Constants.friendType.FOLLOW.getName()).size();
        List<VideoBean> videos = videoDao.queryVideosByUid(userBean.getUserId());
        userHome.setNickName(userBean.getUserNickname());
        userHome.setHeardUrl(userBean.getHeadimgUrl());
        userHome.setVideoNum(videos.size());
        userHome.setVideos(videos);
        userHome.setFansNum(fansNum);
        userHome.setFollowNum(followNum);
        return userHome;
    }

    /**
     * 添加用户即注册功能
     *
     * @param userName,userPassword,email
     * @param userPassword
     * @param email
     * @return
     */
    @Override
    @Transactional
    public boolean addUser(String userName, String userPassword, String email) {
        if (StrJudgeUtil.isCorrectStr(userName)
                && StrJudgeUtil.isCorrectStr(userPassword)
                && StrJudgeUtil.isCorrectStr(email)) {
            if ((userName.length() >= 3 && userName.length() < 20) &&
                    (userPassword.length() < 16 && userPassword.length() >= 8)) {
                if (StrJudgeUtil.isEmail(email)) {
                    UserBean userBean = new UserBean();
                    userBean.setUserName(userName);
                    userBean.setUserPassword(userPassword);
                    userBean.setUserEmail(email);
                    try {
                        int res = userDao.insertUser(userBean);
                        if (res > 0) {
                            String code = CodeUtil.generateUniqueCode();
                            String dataCode = System.currentTimeMillis() + "-" + CodeUtil.RandomCode();
                            UserBean user = userDao.queryUserByName(userName);
                            AuxiliaryUserBean auxiliaryUserBean = new AuxiliaryUserBean();
                            auxiliaryUserBean.setUserId(user.getUserId());
                            auxiliaryUserBean.setUserRole(USERROLE.getName());
                            auxiliaryUserBean.setUserCode(code);
                            auxiliaryUserBean.setCode(dataCode);
                            try {
                                int ares = auxiliaryUserDao.insertAuxiliaryUser(auxiliaryUserBean);
                                if (ares > 0) {
                                    String mailType = ACTIVATION.getName();
                                    new Thread(new MailUtil(email, code, mailType)).start();
                                    return true;
                                } else {
                                    return false;
                                }
                            } catch (Exception e) {
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                } else {
                    throw new RuntimeException("邮箱不合法");
                }
            } else {
                throw new RuntimeException("用户名长度最长20位最短3位，密码长度最长16位最短8位");
            }
        } else {
            throw new RuntimeException("输入信息不能为空");
        }
        return false;
    }

    /**
     * 修改用户信息
     *
     * @param userBean
     * @param token
     * @param file
     * @param filePath
     * @return
     */
    @Override
    @Transactional
    @Caching(evict = {@CacheEvict(value = "userAndVideo", allEntries = true),
            @CacheEvict(value = "userCache", allEntries = true),
            @CacheEvict(value = "collAndVideoAndUser", allEntries = true),
            @CacheEvict(value = "userAndFriend", allEntries = true),
            @CacheEvict(value = "videoCache", allEntries = true)
    })
    public boolean modifyUser(UserBean userBean, String token, MultipartFile file, String filePath) {
        if (StrJudgeUtil.isCorrectStr(userBean.getUserNickname()) &&
                StrJudgeUtil.isCorrectStr(userBean.getUserSex()) &&
                StrJudgeUtil.isCorrectStr(userBean.getBardianSign()) &&
                StrJudgeUtil.isCorrectStr(userBean.getUserBirthday())) {
            String fileType = file.getContentType();
            MultipartFile[] files = new MultipartFile[]{file};
            String[] filesPath = new String[]{filePath + HEADPATH.getName()};
            String[] filesName = null;
            if (FileUtil.isImageFile(fileType)) {
                String ext = FileUtil.fileTypeConvert(fileType);
                String userName = JWTUtil.getUsername(token);
                UserBean userBean1 = userDao.queryUserByName(userName);
                //删除原来的文件
               if(userBean1.getHeadimgUrl()!=null&&!userBean1.equals(" ")){
                   try {
                      fileService.deleteFile(filePath + HEADPATH.getName(), userBean1.getHeadimgUrl());
                   }catch (Exception e){
                       throw new RuntimeException(e.getMessage()+"删除文件出错");
                   }
               }
                String fileName = HEADIMGPREFIX.getName() + userBean1.getUserId() + ext;
                filesName = new String[]{fileName};
                userBean1.setUserNickname(userBean.getUserNickname());
                userBean1.setHeadimgUrl(HEADURL.getName() + fileName);
                userBean1.setUserSex(userBean.getUserSex());
                userBean1.setBardianSign(userBean.getBardianSign());
                userBean1.setUserBirthday(userBean.getUserBirthday());
                try {
                    boolean success = fileService.upLoadFile(files, filesPath, filesName);
                    int res = userDao.updateUser(userBean1);
                    if (res > 0 && success) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage() + "修改资料出错");
                }
            } else {
                throw new RuntimeException("修改资料上传文件类型不合法");
            }
        } else {
            throw new RuntimeException("修改资料参数不合法");
        }
    }

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "userAndVideo", allEntries = true),
            @CacheEvict(value = "userCache", allEntries = true),
            @CacheEvict(value = "collAndVideoAndUser", allEntries = true),
            @CacheEvict(value = "userAndFriend", allEntries = true),
            @CacheEvict(value = "videoCache", allEntries = true)
    })
    public boolean delUser(int userId) {
        if (StrJudgeUtil.isCorrectInt(userId)) {
            try {
                int row = userDao.deleteUser(userId);
                if (row > 0) {
                    return true;
                } else {
                    throw new RuntimeException("删除用户失败！");
                }
            } catch (Exception e) {
                throw new RuntimeException("删除删除用户出错：" + e.getMessage());
            }
        } else {
            throw new RuntimeException("用户id参数不合法");
        }
    }

    /**
     * 获取用户朋友列表
     *
     * @param token
     * @param friendType
     * @return
     */
    @Override
    @Cacheable(value = "userAndFriend")
    public List<UserBean> goFriendList(String token, String friendType) {
        UserBean userBean = this.findUserByToken(token);
        List<UserBean> friends = userFriendsDao.queryFriendByIdAndType(userBean.getUserId(), friendType);
        for (UserBean user : friends) {
            user.setUserPassword("");
            user.setUserEmail("");
            user.setRegisterTime(null);
        }
        return friends;
    }

    //用户收藏集合
    @Override
    @Cacheable(value = "collAndVideoAndUser")
    public List<VideoBean> goCollectionList(String token) {
        UserBean userBean = this.findUserByToken(token);
        List<VideoBean> videos = collectionDao.queryCollectionByUid(userBean.getUserId());
        return videos;
    }

    //其他用户主页
    @Override
    @Cacheable(value = "userAndVideo")
    public UserHomeBean goUserHomeByUid(int userId) {
        UserHomeBean userHome = new UserHomeBean();
        if (!StrJudgeUtil.isCorrectInt(userId)) {
            throw new RuntimeException("参数不合法");
        }
        UserBean userBean = userDao.queryUserById(userId);
        int fansNum = userFriendsDao.queryFriendByIdAndType(userBean.getUserId(), Constants.friendType.FANS.getName()).size();
        int followNum = userFriendsDao.queryFriendByIdAndType(userBean.getUserId(), Constants.friendType.FOLLOW.getName()).size();
        List<VideoBean> videos = videoDao.queryVideosByUid(userBean.getUserId());
        userHome.setNickName(userBean.getUserNickname());
        userHome.setHeardUrl(userBean.getHeadimgUrl());
        userHome.setVideoNum(videos.size());
        userHome.setVideos(videos);
        userHome.setFansNum(fansNum);
        userHome.setFollowNum(followNum);
        return userHome;
    }


    /**
     * 修改密码
     * 验证码由时间戳和随机码构成，通过切割分别获取到，然后进行比对
     *
     * @param userName,code,password
     * @param code
     * @param password
     * @return
     */
    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "userCache", allEntries = true),
    })
    public boolean modifyPassword(String userName, String code, String password) {
        UserBean userBean = userDao.queryUserByName(userName);
        if (userBean != null) {
            AuxiliaryUserBean auxiliaryUserBean = auxiliaryUserDao.queryAuxiliaryUserById(userBean.getUserId());
            String acode = auxiliaryUserBean.getCode();
            String[] str = acode.split("-");
            long time = Long.valueOf(str[0]);
            if (str[1].equals(code)) {
                if (System.currentTimeMillis() - time < 1000 * 60 * 30) {
                    userBean.setUserPassword(password);
                    try {
                        int res = userDao.updateUser(userBean);
                        if (res > 0) {
                            String dataCode = System.currentTimeMillis() + "-" + CodeUtil.RandomCode();
                            auxiliaryUserBean.setCode(dataCode);
                            int ares = auxiliaryUserDao.updateAuxiliaryUser(auxiliaryUserBean);
                            if (ares > 0) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("出现错误" + e.getMessage());
                    }
                } else {
                    throw new RuntimeException("验证码失效");
                }
            } else {
                throw new RuntimeException("验证码错误,尝试重新获取");
            }
        } else {
            throw new RuntimeException("用户不存在");
        }
    }

    /**
     * 添加关注
     *
     * @param token
     * @param friendId
     * @return
     */
    @Override
    @Transactional
    @CacheEvict(value = "userAndFriend", allEntries = true)
    public boolean addFollow(String token, int friendId) {
        if (StrJudgeUtil.isCorrectInt(friendId)) {
            boolean isFollow = false;
            UserBean userBean = this.findUserByToken(token);
            List<UserBean> userBeans = userFriendsDao.queryFriendByIdAndType(userBean.getUserId(), Constants.friendType.FOLLOW.getName());
            if (userBean.getUserId() == friendId) {
                throw new RuntimeException("你不能关注自己!");
            }
            for (int i = 0; i < userBeans.size(); i++) {
                if (friendId == userBeans.get(i).getUserId()) {
                    isFollow = true;
                    throw new RuntimeException("你已经关注过该好友了");
                }
            }
            if (!isFollow) {
                UserFriendsBean user = new UserFriendsBean();
                user.setUserId(userBean.getUserId());
                user.setFriendId(friendId);
                user.setFriendType(Constants.friendType.FOLLOW.getName());
                UserFriendsBean userFriend = new UserFriendsBean();
                userFriend.setUserId(friendId);
                userFriend.setFriendId(userBean.getUserId());
                userFriend.setFriendType(Constants.friendType.FANS.getName());
                try {
                    int res = userFriendsDao.insertUserFriend(user);
                    int res2 = userFriendsDao.insertUserFriend(userFriend);
                    if (res > 0 && res2 > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
        return false;
    }

    /**
     * 取消关注
     *
     * @param token
     * @param friendId
     * @return
     */
    @Override
    @Transactional
    public boolean cancelFollow(String token, int friendId) {
        if (StrJudgeUtil.isCorrectInt(friendId)) {
            UserBean userBean = this.findUserByToken(token);
            if (userBean.getUserId() == friendId) {
                throw new RuntimeException("操作异常");
            }
            List<UserBean> userBeans = userFriendsDao.queryFriendByIdAndType(userBean.getUserId(), Constants.friendType.FOLLOW.getName());
            for (int i = 0; i < userBeans.size(); i++) {
                if (friendId == userBeans.get(i).getUserId()) {
                    int res = userFriendsDao.deleteUserFriend(userBeans.get(i).getUserId(), friendId);
                    if (res > 0) {
                        return true;
                    } else {
                        throw new RuntimeException("你还没有关注他");
                    }
                }
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
        return false;
    }

    @Override
    public boolean collectUserVideo(String token, int videoId) {
        if (StrJudgeUtil.isCorrectInt(videoId)) {
            UserBean userBean = this.findUserByToken(token);
            List<VideoBean> videoBeans = collectionDao.queryCollectionByUid(userBean.getUserId());
            boolean isCanCollect = true;
            for (int i = 0; i < videoBeans.size(); i++) {
                if (videoBeans.get(i).getVideoId() == videoId) {
                    isCanCollect = false;
                    throw new RuntimeException("你已经收藏过该作品");
                }
                if (videoBeans.get(i).getUserBean().getUserId() == userBean.getUserId()) {
                    isCanCollect = false;
                    throw new RuntimeException("你不能收藏自己的作品");
                }
            }
            if (isCanCollect) {
                try {
                    CollectionBean collectionBean = new CollectionBean();
                    collectionBean.setUserBean(userBean);
                    collectionBean.setVideoId(videoId);
                    collectionBean.setUserId(userBean.getUserId());
                    int res = collectionDao.insertCollection(collectionBean);
                    if (res > 0) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    throw new RuntimeException("异常" + e.getMessage());
                }
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
        return false;
    }

    @Override
    public boolean cancelCollectUserVideo(String token, int videoId) {
        if (StrJudgeUtil.isCorrectInt(videoId)) {
            UserBean userBean = this.findUserByToken(token);
            List<VideoBean> videoBeans = collectionDao.queryCollectionByUid(userBean.getUserId());
            for (int i = 0; i < videoBeans.size(); i++) {
                if (videoBeans.get(i).getVideoId() == videoId) {
                    try {
                        int res = collectionDao.deleteUserCollection(userBean.getUserId(), videoBeans.get(i).getVideoId());
                        if (res > 0) {
                            return true;
                        } else {
                            throw new RuntimeException("你还没有收藏该作品");
                        }
                    } catch (Exception e) {
                        throw new RuntimeException("异常" + e.getMessage());
                    }
                }
                if (videoBeans.get(i).getUserBean().getUserId() == userBean.getUserId()) {
                    throw new RuntimeException("出错了");
                }
            }
        } else {
            throw new RuntimeException("参数不合法");
        }
        return false;
    }


}
