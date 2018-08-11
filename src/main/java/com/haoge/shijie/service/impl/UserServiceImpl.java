package com.haoge.shijie.service.impl;

import com.haoge.shijie.dao.*;
import com.haoge.shijie.pojo.*;
import com.haoge.shijie.pojo.respModelBean.UserHomeBean;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.UserService;
import com.haoge.shijie.util.*;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

@Service
@Component
public class UserServiceImpl implements UserService {
    private final static String S = java.io.File.separator;
    private final static String FANS="fans";
    private final static String FOLLOW="follow";
    private final static String VIDEOURL="http://www.haogedada.top/api/upLoadFile/videoFile/";
    private final static String HEADURL="http://www.haogedada.top/api/upLoadFile/pictureFile/headImage/";
    private final static String VIDEOCOVERURL="http://www.haogedada.top/api/upLoadFile/videoCover/";
    private final static String VIDEOPATH="videoFile"+S;
    private final static String HEADPATH="pictureFile"+S+"headImage"+S;
    private final static String VIDEOCOVERPATH="videoCover"+S;
    //private final static String VIDEOPATH="http://127.0.0.1:8080/upLoadFile/videoFile/";
    //private final static String HEADPATH="http://127.0.0.1:8080/upLoadFile/pictureFile/headImage/";
    //private final static String VIDEOPATH="http://127.0.0.1:8080/upLoadFile/videoFile/";
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
    private FileUtil fileUtil;

    @Override
    public List<UserBean> findUsers() {
        return userDao.queryUserList();
    }
    @Override
    public UserBean findUserById(Integer userId) {
        return userDao.queryUserById(userId);
    }

    @Override
    public UserBean findUserAndAuxById(Integer userId) {
        return  userDao.queryUserAndAuxById(userId);
    }
    @Override
    public UserBean findUserAndCollById(Integer userId) {
        return  userDao.queryUserAndCollById(userId);
    }
    @Override
    public UserBean findUserAndFriendById(Integer userId,String friendType) {
        return  userDao.queryUserAndFriendById(userId,friendType);
    }
    @Override
    public UserBean findUserAndVdoById(Integer userId) {
        return  userDao.queryUserAndVdoById(userId);
    }


    @Override
    public UserBean findUserByToken(String token) {
        String userName=JWTUtil.getUsername(token);
        UserBean userBean = userDao.queryUserByName(userName);
        userBean.setUserPassword("");
        return userBean;
    }
    @Override
    public UserHomeBean goUserHomeByToken(String token) {
        String userName=JWTUtil.getUsername(token);
        UserHomeBean userHome=new UserHomeBean();
        UserBean userBean = userDao.queryUserByName(userName);
        int fansNum=userFriendsDao.queryFriendByIdAndType(userBean.getUserId(),FANS).size();
        int followNum=userFriendsDao.queryFriendByIdAndType(userBean.getUserId(),FOLLOW).size();
        List<VideoBean> videos=videoDao.queryVideosByUid(userBean.getUserId());
        userHome.setNickName(userBean.getUserNickname());
        userHome.setHeardUrl(userBean.getHeadimgUrl());
        userHome.setVideoNum(videos.size());
        userHome.setVideos(videos);
        userHome.setFansNum(fansNum);
        userHome.setFollowNum(followNum);
        return userHome;
    }

    @Override
    @Transactional
    public boolean addUser(UserBean user) {
        if((user.getUserName()!=null&&!user.getUserName().equals(""))&&
                (user.getUserPassword()!=null&&!user.getUserPassword().equals(""))
                &&(user.getUserEmail()!=null&&!user.getUserEmail().equals(""))){
            try {
                int row = userDao.insertUser(user);
                if(row>0){
                    return true;
                }else {
                    throw new RuntimeException("插入用户信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("插入用户信息失败："+e.getMessage());
            }
        }else{
            throw new RuntimeException("用户名或密码或邮箱信息不能为空！");
        }
    }

    @Override
    @Transactional
    public boolean addUser(String userName, String userPassword, String email) {
        if(StrJudgeUtil.isCorrectStr(userName)
                &&StrJudgeUtil.isCorrectStr(userPassword)
                &&StrJudgeUtil.isCorrectStr(email)){
            if((userName.length()>=3&&userName.length() < 20)&&
                    (userPassword.length() < 16&&userPassword.length()>=8)) {
                if (StrJudgeUtil.isEmail(email)) {
                    UserBean userBean=new UserBean();
                    userBean.setUserName(userName);
                    userBean.setUserPassword(userPassword);
                    userBean.setUserEmail(email);
                    try {
                        int res=userDao.insertUser(userBean);
                        if(res>0){
                            String code=CodeUtil.generateUniqueCode();
                            String dataCode=System.currentTimeMillis()+"-"+CodeUtil.RandomCode();
                            UserBean user=userDao.queryUserByName(userName);
                            AuxiliaryUserBean auxiliaryUserBean=new AuxiliaryUserBean();
                            auxiliaryUserBean.setUserId(user.getUserId());
                            auxiliaryUserBean.setUserRole("user");
                            auxiliaryUserBean.setUserCode(code);
                            auxiliaryUserBean.setCode(dataCode);
                            try {
                                int ares = auxiliaryUserDao.insertAuxiliaryUser(auxiliaryUserBean);
                                if (ares > 0) {
                                    String mailType="activation";
                                    new Thread(new MailUtil(email, code,mailType)).start();
                                    return true;
                                }else {
                                    return false;
                                }
                            }catch (Exception e){
                                throw new RuntimeException(e.getMessage());
                            }
                        }
                    }catch (Exception e){
                       throw new RuntimeException(e.getMessage());
                    }

                }else{
                    throw new RuntimeException("邮箱不合法");
                }
            }else {
                throw new RuntimeException("用户名长度最长20位最短3位，密码长度最长16位最短8位");
            }
        }else {
            throw new RuntimeException("输入信息不能为空");
        }
        return false;
    }

    @Override
    @Transactional
    public boolean modifyUser(UserBean userBean,String token,MultipartFile file, String filePath) {
       if(StrJudgeUtil.isCorrectStr(userBean.getUserNickname())&&
          StrJudgeUtil.isCorrectStr(userBean.getUserSex())&&
          StrJudgeUtil.isCorrectStr(userBean.getBardianSign())&&
          StrJudgeUtil.isCorrectStr(userBean.getUserBirthday())){
           String fileType = file.getContentType();
           MultipartFile [] files=new MultipartFile[]{file};
           String [] filesPath=new String[]{filePath+HEADPATH};
           String [] filesName=null;
           if(FileUtil.isImageFile(fileType)) {
               String username = JWTUtil.getUsername(token);
               String ext = FileUtil.fileTypeConvert(fileType);
               UserBean userBean1 = userDao.queryUserByName(username);
               String fileName=userBean.getUserId()+ext;
               filesName=new String[]{fileName};
               userBean1.setUserNickname(userBean.getUserNickname());
               userBean1.setHeadimgUrl(HEADURL+fileName);
               userBean1.setUserSex(userBean.getUserSex());
               userBean1.setBardianSign(userBean.getBardianSign());
               userBean1.setUserBirthday(userBean.getUserBirthday());
               try {
                  boolean success= upLoadFile(files,filesPath,filesName);
                   int res=userDao.updateUser(userBean);
                   if(res>0&&success){
                       return true;
                   }else {
                      return false;
                   }
               } catch (Exception e) {
                   throw new RuntimeException(e.getMessage()+"修改资料出错");
               }
           }else {
               throw new RuntimeException("修改资料上传文件类型不合法");
           }
       }else {
           throw new RuntimeException("修改资料参数不合法");
       }
    }

    @Override
    @Transactional
    public boolean delUser(Integer userId) {
        if(userId!=null && userId>0){
            try {
                int row = userDao.deleteUser(userId);
                if(row>0){
                    return true;
                }else {
                    throw new RuntimeException("删除区域信息失败！");
                }
            }catch (Exception e){
                throw new RuntimeException("删除区域信息失败："+e.getMessage());
            }
        }else{
            throw new RuntimeException("区域id不能为空！");
        }
    }

    @Override
    public List<UserBean> goFriendList(String token,String friendType) {
        String userName=JWTUtil.getUsername(token);
        UserBean userBean=userDao.queryUserByName(userName);
        List<UserBean> friends=userFriendsDao.queryFriendByIdAndType(userBean.getUserId(),friendType);
        for(UserBean user:friends){
            user.setUserPassword("");
            user.setUserEmail("");
            user.setRegisterTime(null);
        }
        return friends;
    }

    //用户收藏集合
    @Override
    public List<VideoBean> goCollectionList(String token) {
        String userName=JWTUtil.getUsername(token);
        UserBean userBean=userDao.queryUserByName(userName);
        List<VideoBean> videos=collectionDao.queryCollectionByUid(userBean.getUserId());
        return videos;
    }

    //用户主页
    @Override
    public UserHomeBean goUserHomeByUid(Integer userId) {
        UserHomeBean userHome=new UserHomeBean();
        UserBean userBean = userDao.queryUserById(userId);
        int fansNum=userFriendsDao.queryFriendByIdAndType(userBean.getUserId(),FANS).size();
        int followNum=userFriendsDao.queryFriendByIdAndType(userBean.getUserId(),FOLLOW).size();
        List<VideoBean> videos=videoDao.queryVideosByUid(userBean.getUserId());
        userHome.setNickName(userBean.getUserNickname());
        userHome.setHeardUrl(userBean.getHeadimgUrl());
        userHome.setVideoNum(videos.size());
        userHome.setVideos(videos);
        userHome.setFansNum(fansNum);
        userHome.setFollowNum(followNum);
        return userHome;
    }

    //查询数据库中是否存在该email
    @Override
    public boolean isExistenceEmail(String email) {
        int res = userDao.queryUserByEmail(email);
           if(res>0){
               return true;
           }else {
             return false;
           }
    }

    //根据userName查询数据库中是否存在该用户
    @Override
    public boolean isExistenceUser(String userName) {
        try {
            UserBean user = userDao.queryUserByName(userName);
            if(user==null||user.getUserId()==null){
                return false;
            }else{
                return true;
            }
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 添加视频，通过上传文件file的长度判断用户上传文件个数
     * @param title,content,token
     * @param content
     * @param token
     * @param file
     * @return返回保存到数据库资源url的文件名
     */
    @Override
    @Transactional
    public boolean addVideo(String title, String content, String token,MultipartFile[] file,String filePath) {
        String [] fileType=null;
        String [] filesPath = new String[] {filePath+VIDEOPATH,filePath+VIDEOCOVERPATH};
        if(file.length==1){
            fileType = new String[]{file[0].getContentType()};
        }else if(file.length==2){
            fileType = new String[]{file[0].getContentType(), file[1].getContentType()};
        }
        //视频格式的判断
        if(!FileUtil.isVedioFile(fileType[0])){
            throw new RuntimeException("上传视频文件格式错误");
        }else if(file.length==2){
            if(!FileUtil.isImageFile(fileType[1])){
                throw new RuntimeException("上传封面文件格式错误");
            }
        }
        if(StrJudgeUtil.isCorrectStr(title)&&StrJudgeUtil.isCorrectStr(content)){
            UserBean user=userDao.queryUserByName(JWTUtil.getUsername(token));
            Integer videoId=videoDao.queryVideoLastId();
            if(StrJudgeUtil.isCorrectInt(videoId)){
                //根据videoType获取文件后缀名
                String videoext= FileUtil.fileTypeConvert(fileType[0]);
                String videoName=videoId+videoext;
                String coverName=null;
                String [] filesName=null;
                //判断用户是否上传封面，
                if (file.length==1&&fileType.length==1){
                    coverName=videoId+".jpg";
                }else if(file.length==2&&fileType.length==2){
                    String coverext= FileUtil.fileTypeConvert(fileType[1]);
                    coverName=videoId+coverext;
                }
                filesName=new String[] {videoName,coverName};
                try {
                   boolean success= upLoadFile(file,filesPath,filesName);
                   if (success){
                       //获取视频时间并转换为时长制
                       int time=FileUtil.getVideoTime(filesPath[0]+filesName[0]);
                       String videoTime=DateUtil.secToTime(time);
                       VideoBean videoBean=new VideoBean();
                       videoBean.setUserId(user.getUserId());
                       videoBean.setVideoTitle(title);
                       videoBean.setVideoContent(content);
                       videoBean.setPlayerCount(0);
                       videoBean.setVideoTipNum(0);
                       videoBean.setVideoTrampleNum(0);
                       videoBean.setVideoUrl(VIDEOURL+videoName);
                       videoBean.setVideoCoverUrl(VIDEOCOVERURL+coverName);
                       videoBean.setVideoTime(videoTime);
                           int res = videoDao.insertVideo(videoBean);
                           if(res>0){
                               return true;
                           }else {
                               throw new RuntimeException("插入视频失败");
                           }
                   }else {
                       return false;
                   }
                } catch (Exception e) {
                    throw new RuntimeException("上传文件出错"+e.getMessage());
                }
            }
        }
        return false;
    }
    /**
     * 发送验证码邮件，设置邮件类型为验证码
     * @param userName,email
     * @param email
     * @return
     */
    @Override
    @Transactional
    public boolean getUserCode(String userName, String email) {

       UserBean userBean=userDao.queryUserByName(userName);
        if(userBean!=null&&userBean.getUserEmail().equals(email)){
            String code=CodeUtil.RandomCode();
            String dataCode=System.currentTimeMillis()+"-"+code;
            AuxiliaryUserBean auxiliaryUserBean=auxiliaryUserDao.queryAuxiliaryUserById(userBean.getUserId());
            auxiliaryUserBean.setCode(dataCode);
            try{
                int res=auxiliaryUserDao.updateAuxiliaryUser(auxiliaryUserBean);
                if(res > 0){
                    String mailType="verificationCode";
                    new Thread(new MailUtil(email,code,mailType)).start();
                    return true;
                }else {
                    return false;
                }
            }catch (Exception e){
                throw new RuntimeException("出现错误"+e.getMessage());
            }
        }else {
            throw new RuntimeException("用户或邮箱不正确");
        }
    }

    /**
     * 修改密码验证码由时间戳和随机码构成，通过切割分别获取到，然后进行比对
     * @param userName,code,password
     * @param code
     * @param password
     * @return
     */
    @Override
    @Transactional
    public boolean modifyPassword(String userName, String code, String password) {
        UserBean userBean=userDao.queryUserByName(userName);
        if(userBean!=null){
            AuxiliaryUserBean auxiliaryUserBean=auxiliaryUserDao.queryAuxiliaryUserById(userBean.getUserId());
            String acode=auxiliaryUserBean.getCode();
            String [] str=acode.split("-");
            long time=Long.valueOf(str[0]);
            if (str[1].equals(code)) {
            if(System.currentTimeMillis()-time<1000*60*30) {
                    userBean.setUserPassword(password);
                    try {
                        int res=userDao.updateUser(userBean);
                        if(res>0){
                            String dataCode=System.currentTimeMillis()+"-"+CodeUtil.RandomCode();
                            auxiliaryUserBean.setCode(dataCode);
                            int ares=auxiliaryUserDao.updateAuxiliaryUser(auxiliaryUserBean);
                            if(ares>0){
                                return true;
                            }else {
                                return false;
                            }
                        }else {
                            return false;
                        }
                    }catch (Exception e){
                        throw new RuntimeException("出现错误"+e.getMessage());
                    }
                }else {
                throw new RuntimeException("验证码失效");
                }
            }else {
                throw new RuntimeException("验证码错误,尝试重新获取");
            }
        }else {
            throw new RuntimeException("用户不存在");
        }
    }
    /**
     * 上传视频，根据数组file，filePath，fileName的长度来判断上传文件是单个
     * 还是多个，还是用户上传，还是自动生成封面
     * @param file
     * @param filePath
     * @param fileName
     * @return
     * @throws Exception
     */
    @Override
    public boolean upLoadFile(MultipartFile[] file, String [] filePath, String [] fileName) throws Exception {
        Future<String> ok1 = fileUtil.uploadFile(file[0].getBytes(), filePath[0], fileName[0]);
       if(fileName.length==1&&filePath.length==1&&file.length==1){
           for (;;) {
               if (ok1.isDone() ) {
                   return true;
               }
               Thread.sleep(800);
           }
       }else if(file.length==2&&fileName.length==2&&filePath.length==2){
               for (;;) {
                   Future<String> ok2 = fileUtil.uploadFile(file[1].getBytes(), filePath[1], fileName[1]);
                   if (ok1.isDone() && ok2.isDone()) {
                       return true;
                   }
                   Thread.sleep(800);
               }
       }else if(file.length==1&&fileName.length==2&&filePath.length==2){
            for(;;) {
                if (ok1.isDone()) {
                   fileUtil.fetchPic(filePath[0] + fileName[0], filePath[1],fileName[1]);
                    return true;
                }
                Thread.sleep(800);
            }
       }
        return false;
    }
}
