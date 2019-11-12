package com.cfs.ape.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.enums.CommandStatusEnum;
import com.cfs.ape.service.CommandService;
import com.cfs.ape.service.support.AepCommandSupport;
import com.cfs.ape.util.MyComparator;
import com.cfs.ape.util.RedissonUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RPriorityBlockingQueue;
import org.redisson.api.RPriorityQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ApplicationInit implements ApplicationRunner {


    @Autowired
    private Config config;

    @Autowired
    private AepCommandSupport aepCommandSupport;

    @Autowired
    private CommandService commandService;

//    @Autowired
//    private RedissonClient redissonClient;


    /**
     * 处理指令下发命令队列
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<AepCommand> list = commandService.listPreparedOrRetryCommand();
        list.forEach(command ->{
            RedissonClient redissonClient = Redisson.create(config);
            RPriorityQueue<String> priorityQueue = redissonClient.getPriorityQueue(ApplicationConstant.COMMAND_PREPARE_HANDLE_QUEUE);
            priorityQueue.trySetComparator(new MyComparator());
            priorityQueue.add(JSON.toJSONString(command));
        });

        ExecutorService executor = Executors.newFixedThreadPool(4);//newSingledThreadExecutor();
        executor.submit(new Runnable() {
            @Override
            public void run() {

                while (ApplicationConstant.CONSUMME) {
                    RedissonClient redissonClient = Redisson.create(config);
                    RPriorityBlockingQueue<String> priorityQueue = redissonClient.getPriorityBlockingQueue(ApplicationConstant.COMMAND_PREPARE_HANDLE_QUEUE);

                    priorityQueue.trySetComparator(new MyComparator());
                    try {

                            String command = priorityQueue.take();

                            if (StringUtils.isBlank(command)) {
                                continue;
                            }

                            JSONObject commandJson = JSON.parseObject(command);
                            AepCommand aepCommand = JSON.toJavaObject(commandJson, AepCommand.class);

                            aepCommand = aepCommandSupport.deviceCommandCreate(aepCommand);
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
