package com.example.template.controller;

import com.example.template.dto.ExcuteParam;
import com.example.template.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @PostMapping("/execute")
    public List<Map> execute(@RequestBody ExcuteParam excuteParam){
        return this.demoService.excute(excuteParam.getSql(),excuteParam.getParam());
    }

}
