package com.bylz.fgaTest.controller;

import com.bylz.fgaTest.annotation.*;
import com.bylz.fgaTest.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: TestController
 * @Description:
 * @Author chenzhipeng
 * @Date 2021/8/16
 * @Version 1.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    ApiService apiService;

    @Person
    @GetMapping("/api1")
    public ResponseEntity api1(){
        System.out.println("api1");
        return apiService.api1();
    }

    @Staff
    @Person
    @GetMapping("/api2")
    public ResponseEntity api2(){
        System.out.println("api2");
        return apiService.api2();
    }

    @Company
    @GetMapping("/api3")
    public ResponseEntity api3(){
        System.out.println("api3");
        return apiService.api3();
    }
}
