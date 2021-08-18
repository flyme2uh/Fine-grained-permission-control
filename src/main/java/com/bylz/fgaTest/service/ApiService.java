package com.bylz.fgaTest.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ApiService
 * @Description:
 * @Author chenzhipeng
 * @Date 2021/8/17
 * @Version 1.0
 */
@Service
public class ApiService {
    public ResponseEntity api1(){
        System.out.println("api1 service");
        return ResponseEntity.ok("api1");
    }

    public ResponseEntity api2(){
        System.out.println("api2 service");
        return ResponseEntity.ok("api2");
    }

    public ResponseEntity api3(){
        System.out.println("api3 service");
        return ResponseEntity.ok("api3");
    }
}
