package com.bylz.fgaTest.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.yaml.snakeyaml.Yaml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

/**
 * @ClassName: AuthInterceptor
 * @Description:
 * @Author chenzhipeng
 * @Date 2021/8/16
 * @Version 1.0
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String chara = request.getHeader("Chara");
        File file = new File("src/main/resources/authPath.yml");
        FileInputStream inputStream = new FileInputStream(file);
        Yaml yml = new Yaml();
        Map load = yml.load(inputStream);
        ArrayList<String> paths = (ArrayList<String>) load.get(chara);
        for (String path : paths) {
            if (path.equals(request.getRequestURI())){
                System.out.println(request.getRequestURI()+" auth false");
                response.sendError(401);
                return false;
            }
        }
        System.out.println(request.getRequestURI()+" auth success");
        return true;
    }
}
