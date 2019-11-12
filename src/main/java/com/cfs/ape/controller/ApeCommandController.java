package com.cfs.ape.controller;

import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.enums.CommandStatusEnum;
import com.cfs.ape.service.CommandHandleService;
import com.cfs.ape.service.CommandService;
import com.cfs.ape.util.RedissonUtil;
import com.cfs.ape.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

@Controller
@RequestMapping("/apecommand")
public class ApeCommandController {

    @Autowired
    private RedissonUtil redissonUtil;


    @Autowired
    private CommandHandleService commandHandleService;

    /**
     * 指令下发接口
     * @param command
     * @return
     */
    @RequestMapping("/command")
    public ResponseEntity<ResponseResult> comand(@RequestBody AepCommand command) {

        command = commandHandleService.handleCommand(command);
        return ResponseEntity.ok(ResponseResult.success(command));
    }

    @RequestMapping("/push")
    public ResponseEntity<ResponseResult> pushToQueue(@RequestParam String queueName,@RequestParam String value){

        boolean result = redissonUtil.pushToPriorityQueue(queueName,value);
        if(result){
            return ResponseEntity.ok(ResponseResult.success());
        }
        return ResponseEntity.ok(ResponseResult.errMsg("failure"));
    }

}
