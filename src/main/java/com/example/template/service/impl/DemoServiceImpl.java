package com.example.template.service.impl;

import com.example.template.service.DemoService;
import com.github.mybatisdq.MyBatisDynamicQueryer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private MyBatisDynamicQueryer myBatisDynamicQueryer;

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
}
