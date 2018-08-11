package com.haoge.shijie.Interceptor;

import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.service.UserService;
import com.haoge.shijie.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
//拦截器
@Component
public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;
    /**
     * 进入controller层之前拦截请求
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
       // System.out.println("---------------------开始进入请求地址拦截----------------------------");
//        if(isLoginAttempt(request,response)) {
//            String token = request.getHeader("Authorization");
//            String username=null;
//            try {
//                 username=JWTUtil.getUsername(token);
//            }catch (Exception e){
//                e.printStackTrace();
//                return false;
//            }
//            Date date=JWTUtil.getTokenDate(token);
//            if(username!=null&&!username.equals("")&&
//                    (System.currentTimeMillis() - date.getTime()<=1000*60*60*30)){
//                    UserBean userBean=userService.findUserByToken(token);
//                    if(userBean!=null&&userBean.getUserName()!=null){
//                        return true;
//                    }else {
//                        return false;
//                    }
//                }else {
//                    return false;
//                }
//            }else {
//                return false;
//            }
        return true;
    }

    //处理请求完成后视图渲染之前的处理操作
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        //System.out.println("--------------处理请求完成后视图渲染之前的处理操作---------------");
    }

    //视图渲染之后的操作
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //System.out.println("---------------视图渲染之后的操作-------------------------0");
    }
    /**
     * 判断用户是否登入。
     * 检测header里面是否包含Authorization字段即可
     */
    protected boolean isLoginAttempt(HttpServletRequest request, HttpServletResponse response) {
        String authorization = request.getHeader("Authorization");
        return authorization != null;
    }

}
