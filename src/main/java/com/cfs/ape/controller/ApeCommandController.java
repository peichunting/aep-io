package com.cfs.ape.controller;

import com.cfs.ape.entity.AepCommand;
import com.cfs.ape.util.ResponseResult;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/apecommand")
public class ApeCommandController {

    @RequestMapping("/command")
    public ResponseEntity<ResponseResult> comand(AepCommand command) {

        return ResponseEntity.ok(ResponseResult.success());
    }

}
