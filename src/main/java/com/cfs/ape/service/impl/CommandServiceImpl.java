package com.cfs.ape.service.impl;

import com.cfs.ape.service.CommandService;
import com.ctg.ag.sdk.biz.AepCommandClient;
import com.ctg.ag.sdk.biz.AepDataClient;
import com.ctg.ag.sdk.biz.AepDeviceCommandClient;
import com.ctg.ag.sdk.biz.aep_command.TupCommandRequest;
import com.ctg.ag.sdk.biz.aep_device_command.CreateCommandRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommandServiceImpl implements CommandService {

    @Value("${aep.app.key}")
    private String appKey;

    @Value("${aep.app.secret}")
    private String appSecret;


    @Override
    public void createCommand() {
        AepDeviceCommandClient client = AepDeviceCommandClient.newClient().appKey(appKey).appSecret(appSecret).build();

        CreateCommandRequest request = new CreateCommandRequest();
        request.setBody();
        // request.setParam..  	// set your request params here
        try {
            System.out.println(client.CreateCommand(request));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
