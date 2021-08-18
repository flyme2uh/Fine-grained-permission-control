package com.bylz.fgaTest.enhancement.impl;

import com.bylz.fgaTest.enhancement.BasePreEnhancement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: PreEnhancement01
 * @Description:
 * @Author chenzhipeng
 * @Date 2021/8/17
 * @Version 1.0
 */
public class PreEnhancement01 implements BasePreEnhancement {
    @Override
    public void preEnhancement(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("enhancement01");
    }
}
