package com.haoge.shijie.controller;


import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {
   // @RequiresAuthentication 登入的用户才可以进行访问
   // @RequiresRoles("admin") admin的角色用户才可以登入
//    @GetMapping("/article") 所有人都可以访问，但是用户与游客看到的内容不同
//    public ResponseBean article() {
//        Subject subject = SecurityUtils.getSubject();
//        if (subject.isAuthenticated()) {//判断是游客还是用户
    //用户
//            return new ResponseBean(200, "You are already logged in", null);
//        } else {
    //游客
//            return new ResponseBean(200, "You are guest", null);
//        }
//    }

}
