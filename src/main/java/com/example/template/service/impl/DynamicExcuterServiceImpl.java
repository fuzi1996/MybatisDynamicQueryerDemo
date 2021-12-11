package com.example.template.service.impl;

import com.example.template.mapper.DemoMapper;
import com.example.template.service.DynamicExcuterService;
import com.github.mybatisdq.MyBatisDynamicQueryer;
import com.github.mybatisdq.constant.DynamicSelectConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamicExcuterServiceImpl implements DynamicExcuterService {
    @Autowired
    private MyBatisDynamicQueryer myBatisDynamicQueryer;

    @Autowired
    private DemoMapper demoMapper;

    /**
     * @param sqlScript sql字符串
     * @param param 查询参数
     * @return
     */
    @Override
    public List<Map> excute(String sqlScript,Object param){
        List<Map> result = this.myBatisDynamicQueryer.selectList(sqlScript, param, Map.class);
        return result;
    }

    @Override
    public List<Map<String, Object>> selectByProvider(String sqlScript, Map<String,Object> param) {
        if(null == param){
            param = new HashMap<>();
        }
        param.put(DynamicSelectConstant.getDefaultSqlValueKey(),sqlScript);
        return this.demoMapper.dynamicQueryer(param);
    }
}
