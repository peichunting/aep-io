package com.cfs.ape.service.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfs.ape.entity.AepCreateDevice;
import com.cfs.ape.enums.CommandStatusEnum;
import com.cfs.ape.service.CreateDeviceService;
import com.ctg.ag.sdk.biz.AepDeviceManagementClient;
import com.ctg.ag.sdk.biz.aep_device_management.CreateDeviceRequest;
import com.ctg.ag.sdk.biz.aep_device_management.CreateDeviceResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AepCreateDeviceSupport {

    Logger logger = LoggerFactory.getLogger(AepCreateDeviceSupport.class);

    @Value("${aep.app.key}")
    private String appKey;

    @Value("${aep.app.secret")
    private String appSecret;

    @Autowired
    private CreateDeviceService createDeviceService;

    public AepCreateDevice createDevice(AepCreateDevice createDevice){
        AepDeviceManagementClient client = AepDeviceManagementClient.newClient()
                .appKey(appKey).appSecret(appSecret)
                .build();

        CreateDeviceRequest request = new CreateDeviceRequest();
        request.setParamMasterKey(createDevice.getMasterKey());
        JSONObject requestJson = new JSONObject();
        requestJson.put("deviceName",createDevice.getDeviceName());
        requestJson.put("deviceSn",createDevice.getDeviceSn());
        requestJson.put("imei",createDevice.getImei());
        requestJson.put("operator",createDevice.getOperator());
        requestJson.put("productId",createDevice.getProductId());
        JSONObject  otherJson = new JSONObject();
        otherJson.put("autoObserver",createDevice.getAutoObserver());
        otherJson.put("imsi",createDevice.getImsi());
        otherJson.put("pskValue",createDevice.getPskValue());
        requestJson.put("other",otherJson);
        request.setBody(otherJson.toJSONString().getBytes());

        try {
            String createDeviceJsonStr = JSON.toJSONString(createDevice);
            logger.info("create device:{} begin process",createDeviceJsonStr);
            CreateDeviceResponse response = client.CreateDevice(request);
            createDevice.setCommandStatus(CommandStatusEnum.FAILURE);
            if(response != null){
                if( 200 == response.getStatusCode()){
                    String content = new String(response.getBody());
                    if(StringUtils.isNotBlank(content)){
                        logger.info("create device: {} result: {}",createDeviceJsonStr,content);
                        JSONObject contentJson = JSON.parseObject(content);
                        int code = contentJson.getInteger("code");
                        if( 200 == code){
                            createDevice.setCommandStatus(CommandStatusEnum.SUCCESS);
                        }
                        String msg = contentJson.getString("msg");
                        String result = contentJson.getString("result");
                        String deviceId = contentJson.getString("deviceId");
                        createDevice.setMsg(msg);
                        createDevice.setDeviceId(deviceId);
                        createDevice.setResult(result);
                    }
                }
            }
            createDeviceService.updateCreateDevice(createDevice);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            client.shutdown();
        }
        return createDevice;
    }
}
