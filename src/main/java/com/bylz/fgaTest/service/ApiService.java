package com.bylz.fgaTest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ResponseEntity api1(){
        log.info("Api1 service");
        return ResponseEntity.ok("api1");
    }

    public ResponseEntity api2(){
        log.info("Api2 service");
        return ResponseEntity.ok("api2");
    }

    public ResponseEntity api3(){
        log.info("Api3 service");
        return ResponseEntity.ok("api3");
    }
}
