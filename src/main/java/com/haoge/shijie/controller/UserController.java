package com.haoge.shijie.controller;

import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.constant.Constants;
import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.pojo.VideoBean;
import com.haoge.shijie.pojo.respModelBean.UserHomeBean;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UserController {
    private final static String UPLOADPATH = "/upLoadFile/";
    @Autowired
    private UserService userService;


    //返回用户个人信息 通过token
    @GetMapping("/user")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean backUserMsg(@RequestHeader("Authorization") String token) {
        UserBean user = userService.findUserByToken(token);
        return new ResponseBean().successMethod(user);
    }

    //用户信息修改
    @PostMapping("/modifyUser")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean modifyUser(@RequestParam("nickname") String nickName,
                                   @RequestParam("sex") String sex,
                                   @RequestParam("birthday") String birthday,
                                   @RequestParam("sign") String sign,
                                   @RequestHeader("Authorization") String token,
                                   @RequestParam("imgfile") MultipartFile file,
                                   HttpServletRequest request
    ) {
        UserBean userBean = new UserBean();
        userBean.setUserNickname(nickName);
        userBean.setUserSex(sex);
        userBean.setUserBirthday(birthday);
        userBean.setBardianSign(sign);
        String filePath = request.getServletContext().getRealPath(UPLOADPATH);
        boolean success = userService.modifyUser(userBean, token, file, filePath);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "修改个人资料失败");
        }
    }

    //返回用户个人首页 通过token
    @GetMapping("/user/userhome")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean backUserHome(@RequestHeader("Authorization") String token) {
        UserHomeBean userHome = userService.goUserHomeByToken(token);
        return new ResponseBean().successMethod(userHome);
    }

    //返回用户收藏列表
    @GetMapping("/user/collections")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean backCollectionList(@RequestHeader("Authorization") String token) {
        List<VideoBean> videos = userService.goCollectionList(token);
        return new ResponseBean().successMethod(videos);
    }

    //返回用户关注列表
    @GetMapping("/user/followList")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean backFollowList(@RequestHeader("Authorization") String token) {
        List<UserBean> followList = userService.goFriendList(token, Constants.friendType.FOLLOW.getName());
        return new ResponseBean().successMethod(followList);
    }

    //返回用户粉丝列表
    @GetMapping("/user/fansList")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean backFansList(@RequestHeader("Authorization") String token) {
        List<UserBean> fansList = userService.goFriendList(token, Constants.friendType.FANS.getName());
        return new ResponseBean().successMethod(fansList);
    }

    //添加关注
    @PutMapping("/user/follow/{userId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean addFollow(@RequestHeader("Authorization") String token,
                                  @PathVariable("userId") Integer friendId) {
        boolean success = userService.addFollow(token, friendId);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            throw new RuntimeException("添加关注失败");
        }
    }

    //取消关注
    @DeleteMapping("/user/follow/{friendId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean delFollow(@RequestHeader("Authorization") String token,
                                  @PathVariable("friendId") Integer friendId) {
        boolean success = userService.delFollow(token, friendId);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            throw new RuntimeException("取消关注失败");
        }
    }

    //根据userId获取其他用户信息
    @GetMapping("/user/{userId}")
    @RequiresAuthentication
    @SerializedField(includes = {"code", "msg", "data"}, encryptions = {"data"})
    public ResponseBean backUser(@PathVariable("userId") Integer userId) {
        UserHomeBean userHome = userService.goUserHomeByUid(userId);
        return new ResponseBean().successMethod(userHome);
    }
}
