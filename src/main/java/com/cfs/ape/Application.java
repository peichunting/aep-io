package com.cfs.ape;

import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigInteger;

@SpringBootApplication(scanBasePackages = {"com.cfs.ape"})
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }

}
