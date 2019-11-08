package com.cfs.ape;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cfs.ape"})
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }

}
