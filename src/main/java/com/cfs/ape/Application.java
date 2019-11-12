package com.cfs.ape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication(scanBasePackages = {"com.cfs.ape"})
@EnableCaching
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }

}
