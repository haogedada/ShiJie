package com.haoge.shijie.Interceptor;

import com.haoge.shijie.util.JWTUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "MyFilter", urlPatterns = "/upLoadFile/*")
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig arg0) throws ServletException {
        //System.out.println("MyFilter init............");
    }
    //过滤器
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        //System.out.println("MyFilter doFilter.........before");
//        if(isLoginAttempt(request,response)) {
//            HttpServletRequest req = (HttpServletRequest) request;
//            String token = req.getHeader("Authorization");
//            String username=JWTUtil.getUsername(token);
//            Date date=JWTUtil.getTokenDate(token);
//            if(username!=null&&!username.equals("")){
//                if(System.currentTimeMillis() - date.getTime()<=1000*60*60*30) {
//                    chain.doFilter(request, response);
//                   // System.out.println("MyFilter doFilter.........after");
//                }else {
//                    return;
//                }
//            }else {
//                return;
//            }
//        }else {
//            return;
//        }
        chain.doFilter(request, response);
    }
    @Override
    public void destroy() {
        //System.out.println("MyFilter destroy..........");
    }

    /**
     * 判断用户是否登入。
     * 检测header里面是否包含Authorization字段即可
     */
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }
}
