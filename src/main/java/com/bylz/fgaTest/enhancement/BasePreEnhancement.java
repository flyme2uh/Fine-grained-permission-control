package com.bylz.fgaTest.enhancement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public interface BasePreEnhancement {
    //前置增强方法
    public void preEnhancement(HttpServletRequest request, HttpServletResponse response);
}
