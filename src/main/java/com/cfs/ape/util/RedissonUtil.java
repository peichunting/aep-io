package com.cfs.ape.util;

import org.redisson.Redisson;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RPriorityBlockingQueue;
import org.redisson.api.RPriorityQueue;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RedissonUtil {

    private static Logger logger = LoggerFactory.getLogger(RedissonUtil.class);

    private static final long MAX_ID = 65536;

    //@Autowired
   // private RedissonClient redissonClient;

    @Autowired
    private Config config;

    public static RPriorityQueue<String> comandQueue = null;

//    public RPriorityQueue<String> createPriorityQueue(String queueName){
//        RPriorityQueue<String> priorityQueue = redissonClient.getPriorityQueue(queueName);
//        priorityQueue.trySetComparator(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o1.compareTo(o2);
//            }
//        });
//        return priorityQueue;
//    }

    public Long getAtomicLong(String key) throws Exception{
        RedissonClient redissonClient = Redisson.create(config);
        Long result = null;
        try {
            RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
            result = atomicLong.getAndIncrement();
            if(result.compareTo(MAX_ID) >= 0){
                result = 0L;
                atomicLong.set(0L);
            }
        }catch(Exception e){
            throw e;
        }finally{
            redissonClient.shutdown();
        }
        return result;
    }


    public boolean pushToPriorityQueue(String queueName,String value){

        RedissonClient redissonClient = Redisson.create(config);

        RPriorityBlockingQueue<String> priorityQueue = redissonClient.getPriorityBlockingQueue(queueName);

        priorityQueue.trySetComparator(new MyComparator());
        try {


            return priorityQueue.add(value);
        }finally {
            redissonClient.shutdown();
        }
    }
}
