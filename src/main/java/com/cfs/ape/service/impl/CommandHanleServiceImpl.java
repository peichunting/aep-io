package com.cfs.ape.service.impl;

import com.alibaba.fastjson.JSON;
import com.cfs.ape.config.ApplicationConstant;
import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.enums.CommandStatusEnum;
import com.cfs.ape.service.CommandHandleService;
import com.cfs.ape.service.CommandService;
import com.cfs.ape.util.RedissonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommandHanleServiceImpl implements CommandHandleService {

    @Autowired
    private CommandService commandService;

    @Autowired
    private RedissonUtil redissonUtil;

    @Override
    public AepCommand handleCommand(AepCommand aepCommand) {

        aepCommand.setCommandStatus(CommandStatusEnum.PREPARED);

        aepCommand = commandService.saveCommand(aepCommand);
        String commandStr = JSON.toJSONString(aepCommand);
        redissonUtil.pushToPriorityQueue(ApplicationConstant.COMMAND_PREPARE_HANDLE_QUEUE,commandStr);
        return aepCommand;
    }
}
