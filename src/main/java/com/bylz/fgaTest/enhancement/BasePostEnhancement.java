package com.bylz.fgaTest.enhancement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface BasePostEnhancement {
    //后置增强方法
    public void postEnhancement(HttpServletRequest request, HttpServletResponse response);
}
