package com.cfs.ape.util;

import org.redisson.Redisson;
import org.redisson.api.RPriorityQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class RedissonUtil {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private Config config;

    public RPriorityQueue<String> createPriorityQueue(String queueName){
        RPriorityQueue<String> priorityQueue = redissonClient.getPriorityQueue(queueName);
        priorityQueue.trySetComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return priorityQueue;
    }

    public boolean pushToPriorityQueue(String queueName,String value){

        RedissonClient redissonClient = Redisson.create(config);

        RPriorityQueue<String> priorityQueue = redissonClient.getPriorityBlockingQueue(queueName);

        priorityQueue.trySetComparator(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        try {
            return priorityQueue.add(value);
        }finally {
            redissonClient.shutdown();
        }
    }
}
