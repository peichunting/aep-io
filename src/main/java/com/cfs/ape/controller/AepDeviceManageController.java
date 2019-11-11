package com.cfs.ape.controller;

import com.cfs.ape.entity.AepCreateDevice;
import com.cfs.ape.service.DeviceHandleService;
import com.cfs.ape.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/apedevice")
public class AepDeviceManageController {


    @Autowired
    private DeviceHandleService deviceHandleService;


    @RequestMapping("/createDevice")
    public ResponseEntity<ResponseResult> createDevice(@RequestBody AepCreateDevice aepCreateDevice){
        aepCreateDevice = deviceHandleService.handleCreateDevice(aepCreateDevice);
        return ResponseEntity.ok(ResponseResult.success(aepCreateDevice));
    }
}

