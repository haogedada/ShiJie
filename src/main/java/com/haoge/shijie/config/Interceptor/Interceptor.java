package com.haoge.shijie.config.Interceptor;

import com.haoge.shijie.Interceptor.MyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//拦截器配置
@Configuration
public class Interceptor implements WebMvcConfigurer {

    /**
     *注入拦截器
     */
    @Autowired
    private MyInterceptor myInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加拦截器
        registry.addInterceptor(myInterceptor)
                //拦截路径，如需拦截多个 .addPathPatterns()
                .addPathPatterns("/**");
        //.excludePathPatterns("/api/getToken","/success");//不需要拦截的路径
    }

}
