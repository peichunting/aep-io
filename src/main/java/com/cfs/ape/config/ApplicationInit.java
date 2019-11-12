package com.cfs.ape.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.enums.CommandStatusEnum;
import com.cfs.ape.service.support.AepCommandSupport;
import com.cfs.ape.util.MyComparator;
import com.cfs.ape.util.RedissonUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.ObjectListener;
import org.redisson.api.RPriorityBlockingQueue;
import org.redisson.api.RPriorityQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Comparator;
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

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {

                while (ApplicationConstant.CONSUMME) {
                    RPriorityBlockingQueue<String> priorityQueue = redissonClient.getPriorityBlockingQueue(ApplicationConstant.COMMAND_PREPARE_HANDLE_QUEUE);
                    priorityQueue.trySetComparator(new MyComparator());
                    try {

                            String command = priorityQueue.take();

                            if (StringUtils.isBlank(command)) {
                                continue;
                            }

                            JSONObject commandJson = JSON.parseObject(command);
                            AepCommand aepCommand = JSON.toJavaObject(commandJson, AepCommand.class);

                            aepCommand = aepCommandSupport.mockDeviceCommandCreate(aepCommand);
                            if(CommandStatusEnum.RETRAY.equals(aepCommand.getCommandStatus())){
                                if (ApplicationConstant.AEP_MAX_RETRY_TIMES >= aepCommand.getRetryTimes()){
                                    priorityQueue.add(JSON.toJSONString(aepCommand));
                                }
                            }
                        //}
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        redissonClient.shutdown();
                    }

                }

            }

        });


    }
}
