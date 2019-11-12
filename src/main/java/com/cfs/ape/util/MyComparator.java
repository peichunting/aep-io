package com.cfs.ape.util;

import com.alibaba.fastjson.JSON;
import com.cfs.ape.entity.AepCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class MyComparator implements Comparator<String> {

    private static Logger logger = LoggerFactory.getLogger(MyComparator.class);

    @Override
    public int compare(String o1, String o2) {
        try {
            AepCommand o1Command = JSON.parseObject(o1, AepCommand.class);
            AepCommand o2Command = JSON.parseObject(o2,AepCommand.class);
            int o1Priority = o1Command.getPriority();
            int o2Priority = o2Command.getPriority();
            if(o1Priority == o2Priority){
                return o1Command.getCreateTime().compareTo(o2Command.getCreateTime());
            }
            return o1Priority < o2Priority? -1 : 1;
        }catch(Exception e){
            logger.error(e.getMessage());

        }
        return o1.compareTo(o2);
    }
}