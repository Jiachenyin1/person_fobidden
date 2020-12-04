package com.jk.lightPole;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description:
 * @Author: zerofwen
 * @CreateDate: 20/11/19 14:03
 */
@EnableScheduling
@SpringBootApplication
public class LightPoleClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(LightPoleClientApplication.class, args);
    }

}