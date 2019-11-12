package com.cfs.ape.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.enums.CommandStatusEnum;
import com.cfs.ape.mapper.CommandMapper;
import com.cfs.ape.service.CommandService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<AepCommand> listPreparedOrRetryCommand() {
        QueryWrapper<AepCommand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("command_status", CommandStatusEnum.PREPARED).or().eq("command_status",CommandStatusEnum.RETRAY);
        List<AepCommand> list = commandMapper.selectList(queryWrapper);
        return list;
    }
}
