package com.example.template.controller;

import com.example.template.dto.ExcuteParam;
import com.example.template.service.DynamicExcuterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    @Autowired
    private DynamicExcuterService dynamicExcuterService;

    @PostMapping("/execute")
    public List<Map> execute(@RequestBody ExcuteParam excuteParam){
        return this.dynamicExcuterService.excute(excuteParam.getSql(),excuteParam.getParam());
    }

    @PostMapping("/executeByProvider")
    public List<Map<String,Object>> executeByProvider(@RequestBody ExcuteParam excuteParam){
        return this.dynamicExcuterService.selectByProvider(excuteParam.getSql(),excuteParam.getParam());
    }

}
