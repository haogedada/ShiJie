package com.haoge.shijie.controller;

import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.pojo.VideoBean;
import com.haoge.shijie.pojo.respModelBean.UserHomeBean;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.service.UserService;

import com.haoge.shijie.util.FileUtil;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController //@RequestMapping("/user")
public class UserController {
    private final static String FANS="fans";
    private final static String FOLLOW="follow";
    private final static String UPLOADPATH="/upLoadFile/";
    @Autowired
    private UserService userService;
    @Autowired
    private FileUtil fileUtil;
    /** Log4j日志处理 */
    //private static final Logger log = Logger.getLogger(UserController.class);
    //返回用户个人信息 通过token
    @GetMapping("/user")
    @RequiresAuthentication
    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
    public ResponseBean backUserMsg(@RequestHeader("Authorization") String token){
        UserBean user = userService.findUserByToken(token);
        return new ResponseBean().successMethod(user);
    }
    //用户信息修改
    @PutMapping("/user")
    @RequiresAuthentication
    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
    public ResponseBean modifyUser(@RequestParam("nickname") String nickName,
                                   @RequestParam("sex") String sex,
                                   @RequestParam("birthday") String birthday,
                                   @RequestParam("sign") String sign,
                                   @RequestHeader("Authorization") String token,
                                   @RequestParam("imgfile") MultipartFile file,
                                   HttpServletRequest request
                                  ) {
        UserBean userBean=new UserBean();
        userBean.setUserNickname(nickName);
        userBean.setUserSex(sex);
        userBean.setUserBirthday(birthday);
        userBean.setBardianSign(sign);
        String filePath =request.getServletContext().getRealPath(UPLOADPATH);
        boolean success = userService.modifyUser(userBean,token,file,filePath);
        if(success){
            return new ResponseBean().successMethod();
        }else {
            return new ResponseBean().failMethod(500,"修改个人资料失败");
        }
    }
    //返回用户个人首页 通过token
    @GetMapping("/user/userhome")
    @RequiresAuthentication
    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
    public ResponseBean backUserHome(@RequestHeader("Authorization") String token){
        UserHomeBean userHome = userService.goUserHomeByToken(token);
        return new ResponseBean().successMethod(userHome);
    }
    //返回用户收藏列表
    @GetMapping("/user/collections")
    @RequiresAuthentication
    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
    public ResponseBean backCollectionList(@RequestHeader("Authorization") String token){
        List<VideoBean> videos=userService.goCollectionList(token);
        return new ResponseBean().successMethod(videos);
    }

    //返回用户关注列表
    @GetMapping("/user/followList")
    @RequiresAuthentication
    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
    public ResponseBean backFollowList(@RequestHeader("Authorization") String token){
        List<UserBean> followList=userService.goFriendList(token,FOLLOW);
        return new ResponseBean().successMethod(followList);
    }
    //返回用户粉丝列表
    @GetMapping("/user/fansList")
    @RequiresAuthentication
    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
    public ResponseBean backFansList(@RequestHeader("Authorization") String token){
        List<UserBean> fansList=userService.goFriendList(token,FANS);
        return new ResponseBean().successMethod(fansList);
    }

    //根据userId获取其他用户信息
    @GetMapping("/user/{userId}")
    @RequiresAuthentication
    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
    public ResponseBean backUser(@PathVariable("userId") Integer userId) {
        UserHomeBean userHome = userService.goUserHomeByUid(userId);
        return new ResponseBean().successMethod(userHome);
    }

    //获取邮箱验证码
    @GetMapping("/forgetPassword/code")
    @SerializedField(includes={"code","msg","data"})
    public ResponseBean getEmailCode(@RequestParam("username") String userName,
                                     @RequestParam("email") String email){
        boolean success=userService.getUserCode(userName,email);
        if(success){
            return new ResponseBean().successMethod();
        }else {
            return new ResponseBean().failMethod(500,"获取验证码失败");
        }
    }
    //修改密码
    @PutMapping("/forgetPassword")
    @SerializedField(includes={"code","msg","data"})
    public ResponseBean modifyPassword(@RequestParam("username") String userName,
                                       @RequestParam("password") String password,
                                       @RequestParam("code") String code){
        boolean success=userService.modifyPassword(userName,code,password);
        if(success){
            return new ResponseBean().successMethod();
        }else {
            return new ResponseBean().failMethod(500,"修改密码失败");
        }
    }

    //视频上传
    @PostMapping("/user/video")
    @RequiresAuthentication
    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
    public ResponseBean upLoadVideo(@RequestParam("file") MultipartFile[] file,
                                  @RequestParam("title") String title,
                                  @RequestParam("content") String content,
                                  @RequestHeader("Authorization") String token,
                                          HttpServletRequest request)   {

        System.out.println(file.length+"|"+file[0].isEmpty());
        String filePath=request.getServletContext().getRealPath(UPLOADPATH);
        boolean success=userService.addVideo(title,content,token,file,filePath);
        //String fileName = file.getOriginalFilename();
        if(success){
            return new ResponseBean().successMethod();
        }else {
            return new ResponseBean().failMethod(500,"上传失败");
        }
    }

    //获取视频
    @GetMapping("/user/video")
    @SerializedField(includes={"code","msg","data"})
    public ResponseBean getVideo(@RequestParam("videoId") Integer videoId){
        VideoBean videoBean=userService.findVideo(videoId);
        return new ResponseBean().successMethod(videoBean);
    }
    //删除视频
    @DeleteMapping("/user/video")
    @SerializedField(includes={"code","msg","data"})
    public ResponseBean delVideo(@RequestHeader("Authorization") String token,
                                       @RequestParam("videoId") Integer videoId
                                      ){
        boolean success=userService.delVideo(token,videoId);
        if(success){
            return new ResponseBean().successMethod();
        }else {
            return new ResponseBean().failMethod(500,"删除失败");
        }
    }
    //修改视频
    @PutMapping("user/video")
    @SerializedField(includes={"code","msg","data"})
    public ResponseBean modifyVideo(@RequestHeader("Authorization") String token,
                                    @RequestParam("videoId") Integer videoId,
                                    @RequestParam("title") String videoTitle,
                                    @RequestParam("content") String videoContent,
                                    @RequestParam("coverfile") MultipartFile coverFile,
                                     HttpServletRequest request){
        if(coverFile.isEmpty()){
            throw new RuntimeException("上传文件为空");
        }
        VideoBean videoBean=new VideoBean();
        videoBean.setVideoId(videoId);
        videoBean.setVideoTitle(videoTitle);
        videoBean.setVideoContent(videoContent);
        String filePath=request.getServletContext().getRealPath(UPLOADPATH);
        boolean success=userService.modifyVideo(videoBean,token,filePath,coverFile);
        if (success){
            return new ResponseBean().successMethod();
        }else {
            return new ResponseBean().failMethod(500,"修改失败");
        }

    }

//
//    @GetMapping("/users")
//    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
//    public ResponseBean gouserlist(){
//        List<UserBean> list = userService.findUsers();
//        return new ResponseBean().successMethod(list);
//    }
//    @PostMapping("/users")
//    public @ResponseBody ResponseBean goinsteruser(@RequestBody UserBean user){
//        boolean res=userService.addUser(user);
//        if(res){
//            return new ResponseBean().successMethod();
//        }else {
//            return new ResponseBean().failMethod(0,"fail");
//        }
//    }
//    @PutMapping("/users")
//    public  @ResponseBody ResponseBean updataArea(@RequestBody UserBean user){
//        boolean res = userService.modifyUser(user);
//        if(res){
//            return new ResponseBean().successMethod();
//        }else {
//            return new ResponseBean().failMethod(0,"fail");
//        }
//    }
//    @DeleteMapping("/users/{id}")
//    public  @ResponseBody ResponseBean deleteArea(@PathVariable("id") Integer userId){
//        boolean res = userService.delUser(userId);
//        if(res){
//            return new ResponseBean().successMethod();
//        }else {
//            return new ResponseBean().failMethod(0,"fail");
//        }
//    }
//    @RequestMapping(path = "/401")
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    public ResponseBean unauthorized() {
//        return new ResponseBean(401,"Unauthorized");
//    }

}
