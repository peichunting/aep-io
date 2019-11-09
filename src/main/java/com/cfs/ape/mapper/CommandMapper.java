package com.cfs.ape.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cfs.ape.entity.AepCommand;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommandMapper extends BaseMapper<AepCommand> {
}
