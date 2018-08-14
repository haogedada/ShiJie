package com.haoge.shijie.Interceptor;

import com.haoge.shijie.pojo.UserBean;
import com.haoge.shijie.service.LoginService;
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

    //token失效的5分钟内进行提示
    private static final long SHIXIAOTIME = 5 * 60 * 1000;
    @Autowired
    private LoginService loginService;

    /**
     * 进入controller层之前拦截请求
     *
     * @param request
     * @param response
     * @param o
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // System.out.println("---------------------开始进入请求地址拦截----------------------------");
        try {
            String token = request.getHeader("Authorization");
            if (token == null || token.equals("")) {
                return true;
            }
            // 解密获得date，用于和当前时间进行对比
            Date tokenTime = JWTUtil.getTokenDate(token);
            //token即将失效，提示前端自己重新获取一个新的token
            if ((tokenTime.getTime() - System.currentTimeMillis()) <= SHIXIAOTIME) {
                String userName = JWTUtil.getUsername(token);
                UserBean userBean = loginService.findUserByName(userName);
                if (userBean != null && userBean.getUserName() != null) {
                    String newToken = JWTUtil.sign(userBean.getUserName(), userBean.getUserPassword());
                    response.addHeader("authorization", newToken);
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return true;
    }

    //处理请求完成后视图渲染之前的处理操作
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        //System.out.println("--------------处理请求完成后视图渲染之前的处理操作---------------");
    }

    //视图渲染之后的操作
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) throws Exception {
        // System.out.println("---------------视图渲染之后的操作-------------------------");
    }
}
