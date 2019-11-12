package com.cfs.ape.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.entity.AepCommandInfo;
import com.cfs.ape.mapper.CommandInfoMapper;
import com.cfs.ape.service.CommandInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ComandInfoServiceImpl implements CommandInfoService {

    @Autowired
    private CommandInfoMapper commandInfoMapper;

    @Override
    @Cacheable(value="commandInfo",key="'commandId:'.concat(#commandType)")
    public AepCommandInfo getCommandInfoByCommandType(int commandType) {
        QueryWrapper<AepCommandInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("command_type",commandType);
        return commandInfoMapper.selectOne(queryWrapper);
    }

    @Override
    @CacheEvict(value="commandInfo",key="'commandId:'.concat(#commandType)")
    public AepCommandInfo updateAepCommandInfo(AepCommandInfo aepCommandInfo){
        commandInfoMapper.updateById(aepCommandInfo);
        return aepCommandInfo;
    }

    @Override
    public AepCommandInfo saveAepCommandInfo(AepCommandInfo aepCommandInfo) {
        commandInfoMapper.insert(aepCommandInfo);
        return aepCommandInfo;
    }
}
