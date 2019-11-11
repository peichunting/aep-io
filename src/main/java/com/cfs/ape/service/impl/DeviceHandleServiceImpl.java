package com.cfs.ape.service.impl;

import com.cfs.ape.entity.AepCreateDevice;
import com.cfs.ape.service.CreateDeviceService;
import com.cfs.ape.service.DeviceHandleService;
import com.cfs.ape.service.support.AepCreateDeviceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceHandleServiceImpl implements DeviceHandleService {

    @Autowired
    private CreateDeviceService createDeviceService;

    private AepCreateDeviceSupport createDeviceSupport;

    @Override
    public AepCreateDevice handleCreateDevice(AepCreateDevice createDevice) {
        createDeviceService.saveCreateDevice(createDevice);
        createDeviceSupport.createDevice(createDevice);
        return createDevice;
    }
}
