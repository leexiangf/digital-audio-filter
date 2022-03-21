package com.zj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zj.mapper")
public class DigitalAudioFilterApplication {

    public static void main(String[] args) {
        SpringApplication.run(DigitalAudioFilterApplication.class, args);
    }

}
