package com.bylz.fgaTest.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String chara = request.getHeader("Chara");
        File file = new File("src/main/resources/authPath.yml");
        FileInputStream inputStream = new FileInputStream(file);
        Yaml yml = new Yaml();
        Map load = yml.load(inputStream);
        ArrayList<String> paths = (ArrayList<String>) load.get(chara);
        if(paths == null){
            log.info("No auth user");
            return true;
        }
        for (String path : paths) {
            if (path.equals(request.getRequestURI())){
                //拦截所有配置的路径和对应的用户角色
                log.info(request.getRequestURI()+" auth false");
                response.sendError(401);
                return false;
            }
        }
        log.info(request.getRequestURI()+" auth success");
        return true;
    }
}
