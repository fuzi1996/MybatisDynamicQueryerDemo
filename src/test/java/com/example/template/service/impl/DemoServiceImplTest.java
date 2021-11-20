package com.example.template.service.impl;


import com.example.template.service.DynamicExcuterService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootTest
class DemoServiceImplTest {

    @Autowired
    private DynamicExcuterService dynamicExcuterService;

    @Test
    public void ifSqlTest() {
        String sql = "select * from students <bind name=\"namePattern\" value=\"'%' + name + '%'\" /><where><if test=\"name != null\">and name like concat('%',#{namePattern},'%')</if><if test=\"name == null\">and 1 = -1</if></where>";

        Map<String,Object> map = new HashMap<>();
        map.put("name","小");
        List<Map> result = this.dynamicExcuterService.excute(sql, map);
        System.out.println(result);
    }

    @Test
    public void staticSqlTest() {
        String sql = "select * from students";
        List<Map> result = this.dynamicExcuterService.excute(sql, null);
        System.out.println(result);
    }

    @Test
    public void includeSqlTest() {
        String sql = "select\n<include refid=\"com.example.template.mapper.DemoController.selectColumns\">\n<property name=\"alias\" value=\"t1\"/>\n</include>\nfrom students t1\nwhere id = #{id}";

        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        List<Map> result = this.dynamicExcuterService.excute(sql, map);
        System.out.println(result);
    }
}
