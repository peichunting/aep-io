package com.cfs.ape.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.cfs.ape.mapper")
public class MybatisPlusConfig {

//    @Bean
//    public GlobalConfig globalConfig(){
//        GlobalConfig conf = new GlobalConfig();
//
//        conf.setWorkerId(1L);
//        return conf;
//    }
}
