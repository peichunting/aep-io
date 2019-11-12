package com.cfs.ape.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfs.ape.bussiness.DataPackageGenerator;
import com.cfs.ape.config.ApplicationConstant;
import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.enums.CommandStatusEnum;
import com.cfs.ape.service.CommandHandleService;
import com.cfs.ape.service.CommandService;
import com.cfs.ape.util.RedissonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.concurrent.ExecutorService;

@Service
public class CommandHanleServiceImpl implements CommandHandleService {

    @Autowired
    private CommandService commandService;

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private DataPackageGenerator dataPackageGenerator;


    @Override
    public AepCommand handleCommand(AepCommand aepCommand)  {

        aepCommand.setCommandStatus(CommandStatusEnum.PREPARED);
        byte[] instruction = dataPackageGenerator.generateDataPackage(aepCommand);
        if(instruction == null){
            throw new IllegalArgumentException("参数错误");
        }
        String hex = new BigInteger(1, instruction).toString(16);
        JSONObject contentJson = new JSONObject();
        contentJson.put("payload",hex);
        contentJson.put("dataType",1);
        contentJson.put("isReturn",1);
        String content = contentJson.toJSONString();
        aepCommand.setContent(content);
        aepCommand = commandService.saveCommand(aepCommand);
        String commandStr = JSON.toJSONString(aepCommand);
        redissonUtil.pushToPriorityQueue(ApplicationConstant.COMMAND_PREPARE_HANDLE_QUEUE,commandStr);
        return aepCommand;
    }
}
