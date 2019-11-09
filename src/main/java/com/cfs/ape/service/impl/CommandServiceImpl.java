package com.cfs.ape.service.impl;

import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.mapper.CommandMapper;
import com.cfs.ape.service.CommandService;
import com.ctg.ag.sdk.biz.AepCommandClient;
import com.ctg.ag.sdk.biz.AepDataClient;
import com.ctg.ag.sdk.biz.AepDeviceCommandClient;
import com.ctg.ag.sdk.biz.aep_command.TupCommandRequest;
import com.ctg.ag.sdk.biz.aep_device_command.CreateCommandRequest;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.CommandMap;
import javax.annotation.Resource;

@Service
public class CommandServiceImpl implements CommandService {

    @Autowired
    private CommandMapper commandMapper;

    @Value("${aep.app.key}")
    private String appKey;

    @Value("${aep.app.secret}")
    private String appSecret;

    @Autowired
    private RedissonClient redissonClient;


    @Override
    public AepCommand saveCommand(AepCommand aepCommand){
        commandMapper.insert(aepCommand);
        return aepCommand;
    }

    @Override
    public AepCommand updateCommand(AepCommand command) {
        commandMapper.updateById(command);
        return command;
    }
}
