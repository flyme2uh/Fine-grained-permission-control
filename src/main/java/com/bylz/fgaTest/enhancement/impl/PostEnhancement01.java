package com.bylz.fgaTest.enhancement.impl;

import com.bylz.fgaTest.enhancement.BasePostEnhancement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: PostEnhancement01
 * @Description:
 * @Author chenzhipeng
 * @Date 2021/8/18
 * @Version 1.0
 */
public class PostEnhancement01 implements BasePostEnhancement {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void postEnhancement(HttpServletRequest request, HttpServletResponse response) {
        log.info("Post enhancement01");
    }
}
