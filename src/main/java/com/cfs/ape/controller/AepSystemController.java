package com.cfs.ape.controller;

import com.cfs.ape.entity.AepCommandInfo;
import com.cfs.ape.service.CommandInfoService;
import com.cfs.ape.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system")
public class AepSystemController {

    @Autowired
    private CommandInfoService commandInfoService;

    @PostMapping("/updateCommandInfo")
    public ResponseEntity<ResponseResult> updateCommandInfo(@RequestBody AepCommandInfo aepCommandInfo){
        aepCommandInfo = commandInfoService.updateAepCommandInfo(aepCommandInfo);
        return ResponseEntity.ok(ResponseResult.success(aepCommandInfo));
    }

    @PostMapping("/saveCommandInfo")
    public ResponseEntity<ResponseResult> saveCommandInfo(@RequestBody AepCommandInfo aepCommandInfo){
        aepCommandInfo = commandInfoService.saveAepCommandInfo(aepCommandInfo);
        return ResponseEntity.ok(ResponseResult.success(aepCommandInfo));
    }
}
