package com.bylz.fgaTest.interceptor;

/**
 * @ClassName: CustomInterceptor
 * @Description:
 * @Author chenzhipeng
 * @Date 2021/8/17
 * @Version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

@Component
public class CustomInterceptor implements HandlerInterceptor {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //前置拦截器
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //配置文件地址
        File file = new File("src/main/resources/preEnhancement.yml");
        String requestURI = request.getRequestURI();
        //读取配置文件中的方法
        ArrayList<String> methodNameList = enhancementYmlReader(requestURI, file);
        //无方法直接通过
        if (methodNameList==null){
            return true;
        }
        //按照配置中的顺序, 反射调用增强类中的增强方法
        for (String methodName : methodNameList) {
            Class<?> clz = Class.forName(methodName);
            Method preEnhancement = clz.getMethod("preEnhancement",HttpServletRequest.class,HttpServletResponse.class);
            preEnhancement.invoke(clz.getConstructor().newInstance(),request,response);
        }
        return true;
    }

    //后置拦截器
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        File file = new File("src/main/resources/postEnhancement.yml");
        String requestURI = request.getRequestURI();
        ArrayList<String> methodNameList = enhancementYmlReader(requestURI, file);
        if(methodNameList != null){
            for (String methodName : methodNameList) {
                Class<?> clz = Class.forName(methodName);
                Method preEnhancement = clz.getMethod("postEnhancement",HttpServletRequest.class,HttpServletResponse.class);
                preEnhancement.invoke(clz.getConstructor().newInstance(),request,response);
            }
        }
    }

    /**
    * @Description: 根据url读取增强配置中的增强方法
    * @Author: chenzhipeng
    * @Date: 2021/8/18
    */
    private ArrayList<String> enhancementYmlReader(String requestURI, File file) throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(file);
        Yaml yaml = new Yaml();
        Map load = yaml.load(inputStream);
        ArrayList<String> methodNameList = (ArrayList<String>) load.get(requestURI);
        return methodNameList;
    }
}
