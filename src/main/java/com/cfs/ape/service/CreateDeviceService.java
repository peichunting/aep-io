package com.cfs.ape.service;

import com.cfs.ape.entity.AepCreateDevice;

public interface CreateDeviceService {

    AepCreateDevice saveCreateDevice(AepCreateDevice createDevice);

    AepCreateDevice updateCreateDevice(AepCreateDevice createDevice);
}
