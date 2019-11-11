package com.cfs.ape.service.impl;

import com.cfs.ape.entity.AepCreateDevice;
import com.cfs.ape.mapper.CreateDeviceMapper;
import com.cfs.ape.service.CreateDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateDeviceServiceImpl implements CreateDeviceService {

    @Autowired
    private CreateDeviceMapper createDeviceMapper;

    @Override
    public AepCreateDevice saveCreateDevice(AepCreateDevice createDevice) {
        createDeviceMapper.insert(createDevice);
        return createDevice;
    }

    @Override
    public AepCreateDevice updateCreateDevice(AepCreateDevice createDevice) {
        createDeviceMapper.updateById(createDevice);
        return createDevice;
    }
}
