package com.bylz.fgaTest.config;

import com.bylz.fgaTest.interceptor.AuthInterceptor;
import com.bylz.fgaTest.interceptor.CustomInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName: MyWebConfig
 * @Description:
 * @Author chenzhipeng
 * @Date 2021/7/22
 * @Version 1.0
 */
@Configuration
public class MyWebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private CustomInterceptor customInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/test/**");
        registry.addInterceptor(customInterceptor)
                .addPathPatterns("/test/**");
    }
}
