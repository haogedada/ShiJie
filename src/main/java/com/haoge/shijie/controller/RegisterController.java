package com.haoge.shijie.controller;


import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.AuxiliaryUserService;
import com.haoge.shijie.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private AuxiliaryUserService Service;
    @Autowired
    private UserService userService;

    //注册
    @PostMapping("/registerfrom")
    @SerializedField(includes={"code","msg","data"})
    public ResponseBean registerFrom(@RequestParam("username") String userName,
                              @RequestParam("password") String userPassword,
                              @RequestParam("email") String email) {
        boolean isregister=userService.addUser(userName,userPassword,email);
    if(isregister){
        return new ResponseBean().successMethod();
    }
        return new ResponseBean().failMethod(120,"注册失败");
    }
    //邮箱激活账号
    @GetMapping("/activation")
    @SerializedField(includes={"code","msg","data"})
    public ResponseBean Activation(@RequestParam("code") String code,HttpServletResponse response) throws Exception {
            int success=Service.activationUser(code);
           if(success==0){
              // throw new RuntimeException("激活码失效，请重新注册");
               return new ResponseBean().successMethod("激活码失效，请重新注册");
           }else if(success==1){
              //response.sendRedirect("/login.html");
                return new ResponseBean().successMethod("激活成功，请到登录页面进行登录");
            }else if(success==2||success==-1){
                throw new RuntimeException("activation erro");
            }
        return new ResponseBean().successMethod("activation erro");
    }

    //查询邮箱是否存在
    @GetMapping("/email")
    @SerializedField(includes={"code","msg","data"})
    public ResponseBean isExistenceEmail(@RequestParam("email") String email)  {
        boolean success=userService.isExistenceEmail(email);
        if(success){
            return new ResponseBean().successMethod();
        }else {
            return new ResponseBean().failMethod(500,"邮箱不存在");
        }
    }
    //查询用户是否存在
    @GetMapping("/username")
    @SerializedField(includes={"code","msg","data"})
    public ResponseBean isExistenceUser(@RequestParam("username") String userName) {
        boolean success=userService.isExistenceUser(userName);
        if(success){
            return new ResponseBean().successMethod();
        }else {
            return new ResponseBean().failMethod(500,"用户名不存在");
        }
    }
}
