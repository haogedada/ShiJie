package com.haoge.shijie.shiro;

import com.google.gson.Gson;
import com.haoge.shijie.pojo.response.ResponseBean;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class JWTFilter extends BasicHttpAuthenticationFilter {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }

    /**
     * 用户进行登录授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        JWTToken token = new JWTToken(authorization);
        // 如果没有抛出异常则代表登入成功，返回true
        getSubject(request, response).login(token);
        return true;
    }

    /**
     * 所有正常的请求都会经过的方法体
     * <p>
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {

                executeLogin(request, response);

            } catch (Exception e) {
                // System.out.println("非法请求");
                response401(response);
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 给非法请求返回401
     *
     * @param resp
     */
    private void response401(ServletResponse resp) {
        try {
            HttpServletResponse response = (HttpServletResponse) resp;
            PrintWriter out = response.getWriter();
            ResponseBean responseBean = new ResponseBean().failMethod(401, "unlawful request");
            Gson gson = new Gson();
            String errStr = gson.toJson(responseBean);
            out.write(errStr);
            out.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
    }

}


