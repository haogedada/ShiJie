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
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.Future;

@RestController //@RequestMapping("/user")
public class UserController {

    private final static String FANS="fans";
    private final static String FOLLOW="follow";
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
        String fileType = file.getContentType();
        String imgPath =request.getServletContext().getRealPath("/upLoadFile/pictureFile/headImage/");
        String imgName = userService.modifyUser(nickName,sex,birthday,sign,token,fileType);
        MultipartFile [] fileArr=new MultipartFile[]{file};
        String [] filePath=new String[]{imgPath};
        String [] fileName=new String[]{imgName};
        if(!imgName.equals("fail")){
            try {
                boolean success=userService.upLoadFile(fileArr,filePath,fileName);
            if(success){
                return new ResponseBean().successMethod();
            }else {
                return new ResponseBean().failMethod(500,"文件上传失败");
            }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            return new ResponseBean().failMethod(500,"修改个人资料失败");
        }
        return new ResponseBean().failMethod(500,"修改个人资料失败");
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

    //处理文件上传
    @PostMapping("/user/upload/video")
    @RequiresAuthentication
    @SerializedField(includes={"code","msg","data"},encryptions = {"data"})
    public ResponseBean uploadVideo(@RequestParam("file") MultipartFile[] file,
                                  @RequestParam("title") String title,
                                  @RequestParam("content") String content,
                                  @RequestHeader("Authorization") String token,
                                          HttpServletRequest request)   {
        String [] fileName=userService.addVideo(title,content,token,file);
        //String fileName = file.getOriginalFilename();
        String videoPath = request.getSession().getServletContext().getRealPath("/upLoadFile/videoFile/");
        String videoCoverPath=request.getSession().getServletContext().getRealPath("/upLoadFile/videoCover/");
        String [] filePath=new String[]{videoPath,videoCoverPath};
        if(fileName[0].equals("b")){
            return new ResponseBean().failMethod(500,"b上传失败");
        }else if(fileName[0].equals("a")){
            return new ResponseBean().failMethod(500,"上传文件格式错误");
        }else {
            try {
                boolean success=userService.upLoadFile(file,filePath,fileName);
                if(success){
                    return new ResponseBean().successMethod();
                }else {
                    return new ResponseBean().failMethod(500,"上传失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResponseBean().failMethod(500,"出错啦");
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
