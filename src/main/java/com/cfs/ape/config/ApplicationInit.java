package com.cfs.ape.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.service.support.AepCommandSupport;
import com.cfs.ape.util.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RPriorityBlockingQueue;
import org.redisson.api.RPriorityQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ApplicationInit implements ApplicationRunner {

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private Config config;

    @Autowired
    private AepCommandSupport aepCommandSupport;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {
                while(ApplicationConstant.CONSUMME){
                    RedissonClient client = Redisson.create(config);
                    RPriorityBlockingQueue<String> priorityQueue = client.getPriorityBlockingQueue(ApplicationConstant.COMMAND_PREPARE_HANDLE_QUEUE);
                    try {
                        String command = priorityQueue.take();
                        JSONObject commandJson = JSON.parseObject(command);
                        AepCommand aepCommand = JSON.toJavaObject(commandJson, AepCommand.class);
                        aepCommandSupport.mockDeviceCommandCreate(aepCommand);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }
}
