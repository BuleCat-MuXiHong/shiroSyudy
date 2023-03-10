package com.shiro;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log4j2
@SpringBootApplication
public class ShiroStudyApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShiroStudyApplication.class, args);
        log.info("项目启动完毕!!!");
    }
}
