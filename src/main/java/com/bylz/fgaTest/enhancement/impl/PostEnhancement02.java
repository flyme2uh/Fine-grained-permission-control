package com.bylz.fgaTest.enhancement.impl;

import com.bylz.fgaTest.enhancement.BasePostEnhancement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: PostEnhancement01
 * @Description:
 * @Author chenzhipeng
 * @Date 2021/8/18
 * @Version 1.0
 */
public class PostEnhancement02 implements BasePostEnhancement {
    @Override
    public void postEnhancement(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("post enhancement02");
    }
}
