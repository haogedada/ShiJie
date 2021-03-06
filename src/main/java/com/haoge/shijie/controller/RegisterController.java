package com.haoge.shijie.controller;


import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.AuxiliaryUserService;
import com.haoge.shijie.service.RegisterService;
import com.haoge.shijie.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(tags = "注册控制器")
@RestController
public class RegisterController {

    @Autowired
    private AuxiliaryUserService Service;
    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserService userService;

    //注册
    @PostMapping("/register")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean registerFrom(@RequestParam("username") String userName,
                                     @RequestParam("password") String userPassword,
                                     @RequestParam("email") String email) {
        boolean isregister = userService.addUser(userName, userPassword, email);
        if (isregister) {
            return new ResponseBean().successMethod();
        }
        return new ResponseBean().failMethod(120, "注册失败");
    }

    //邮箱激活账号
    @GetMapping("/register/activation")
    @SerializedField(includes = {"code", "msg", "data"})
    public void Activation(@RequestParam("code") String code,
                           HttpServletResponse response) throws Exception {
        boolean success = Service.activationUser(code);
        if (success) {
            response.sendRedirect("http://www.haogedada.top/apiep/success.html");
           // return new ResponseBean().successMethod("激活成功，请到登录页面进行登录");
        } else {
          //  return new ResponseBean().successMethod("激活失败");
            response.sendRedirect("http://www.haogedada.top/apiep/fail.html");
        }
    }

    //查询邮箱是否存在
    @GetMapping("/email")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean isExistenceEmail(@RequestParam("email") String email) {
        boolean success = registerService.isExistenceEmail(email);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "邮箱不存在");
        }
    }

    //查询用户是否存在
    @GetMapping("/username")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean isExistenceUser(@RequestParam("username") String userName) {
        boolean success = registerService.isExistenceUser(userName);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "用户名不存在");
        }
    }

    //获取邮箱验证码
    @GetMapping("/forgetPassword/code")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean getEmailCode(@RequestParam("username") String userName,
                                     @RequestParam("email") String email) {
        boolean success = registerService.getUserCode(userName, email);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "获取验证码失败");
        }
    }

    //修改密码
    @PutMapping("/forgetPassword")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean modifyPassword(@RequestParam("username") String userName,
                                       @RequestParam("password") String password,
                                       @RequestParam("code") String code) {
        boolean success = userService.modifyPassword(userName, code, password);
        if (success) {
            return new ResponseBean().successMethod();
        } else {
            return new ResponseBean().failMethod(500, "修改密码失败");
        }
    }
}
