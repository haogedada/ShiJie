package com.haoge.shijie.controller;

import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.LoginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "登录控制器")
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    //登录
    @PostMapping("/login")
    @SerializedField(includes = {"code", "msg", "data"})
    public ResponseBean login(@RequestParam("username") String userName,
                              @RequestParam("password") String userPassword) {
        ResponseBean token = loginService.getToken(userName, userPassword);
        return token;
    }


//    @GetMapping("/usertest")
//    //@RequiresAuthentication//登入的用户才可以进行访问
//    @SerializedField(includes={"code","msg","data"})
//    public ResponseBean requireAuth(@RequestParam("name") String name) {
//        return new ResponseBean().successMethod("ok");
//    }
}
