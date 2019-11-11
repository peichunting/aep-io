package com.cfs.ape.service.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.enums.CommandStatusEnum;
import com.cfs.ape.service.CommandService;
import com.ctg.ag.sdk.biz.AepDeviceCommandClient;
import com.ctg.ag.sdk.biz.aep_device_command.CreateCommandRequest;
import com.ctg.ag.sdk.biz.aep_device_command.CreateCommandResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AepCommandSupport {

    public static final int SUCCESS = 200;

    Logger logger = LoggerFactory.getLogger(AepCommandSupport.class);

    @Value("${aep.app.key}")
    private String appKey;

    @Value("${aep.app.secret")
    private String appSecret;

    @Autowired
    private CommandService commandService;

    public AepCommand mockDeviceCommandCreate(AepCommand aepCommand){
        aepCommand.setCommandStatus(CommandStatusEnum.SUCCESS);
        aepCommand.setCommandId(123);
        aepCommand.setCode(200);
        return commandService.updateCommand(aepCommand);
    }

    public AepCommand deviceCommandCreate(AepCommand aepCommand){
        aepCommand.setCommandStatus(CommandStatusEnum.PROCESSING);
        String commandRequestStr = JSON.toJSONString(aepCommand);
        logger.info("command {} begin process",commandRequestStr);
        commandService.updateCommand(aepCommand);
        try {
            AepDeviceCommandClient client = AepDeviceCommandClient.newClient()
                    .appKey(appKey).appSecret(appSecret)
                    .build();

            CreateCommandRequest request = new CreateCommandRequest();
            request.setParamMasterKey(aepCommand.getMasterKey());
            JSONObject requestJson = new JSONObject();
            requestJson.put("deviceId",aepCommand.getDeviceId());
            String requestContent = aepCommand.getContent();

            requestJson.put("content",JSON.parseObject(requestContent));
// set your request params here
// request.setParamMasterKey(MasterKey);	// single value
// request.addParamMasterKey(MasterKey);	// or multi values
// request.setBody([BINARY DATA])
            CreateCommandResponse response = client.CreateCommand(request);
            int code = response.getStatusCode();

            if (200 != code) {
                aepCommand.setCommandStatus(CommandStatusEnum.FAILURE);
            }else {
                String content = new String(response.getBody());

                if (StringUtils.isNotBlank(content)) {

                    JSONObject jsonContent = JSON.parseObject(content);
                    String resultCode = jsonContent.getString("code");
                    JSONObject result = jsonContent.getJSONObject("result");
                    aepCommand.setCode(Integer.valueOf(resultCode));
                    if ("200".equals(resultCode)) {
                        String commandId = result.getString("commandId");
                        aepCommand.setCommandId(Long.valueOf(commandId));
                        aepCommand.setCommandStatus(CommandStatusEnum.SUCCESS);
                    } else {
                        aepCommand.setCommandStatus(CommandStatusEnum.FAILURE);
                    }

                } else {
                    aepCommand.setCommandStatus(CommandStatusEnum.FAILURE);
                }
            }

        }catch (Exception e){
            aepCommand.setCommandStatus(CommandStatusEnum.FAILURE);
        }
        return commandService.updateCommand(aepCommand);
    }
}
