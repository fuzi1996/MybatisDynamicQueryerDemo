package com.example.template.service.impl;


import com.example.template.SpringBootTemplateApplication;
import com.example.template.mapper.DemoMapper;
import com.example.template.service.DynamicExcuterService;
import com.github.mybatisdq.cache.GlobalConfigurationCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.*;
import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootTest
class DemoServiceImplTest {

    @Autowired
    private DynamicExcuterService dynamicExcuterService;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @BeforeEach
    public void test(){
        GlobalConfigurationCache.setConfiguration(sqlSessionFactory.getConfiguration());
    }

    @Test
    public void ifSqlTest() {
        String sql = "select * from students <bind name=\"namePattern\" value=\"'%' + name + '%'\" /><where><if test=\"name != null\">and name like concat('%',#{namePattern},'%')</if><if test=\"name == null\">and 1 = -1</if></where>";

        Map<String,Object> map = new HashMap<>();
        map.put("name","小");
        List<Map> result = this.dynamicExcuterService.excute(sql, map);
        System.out.println(result);
    }

    @Test
    public void ifSqlTestByDemoMapper() {
        String sql = "select * from students <bind name=\"namePattern\" value=\"'%' + name + '%'\" /><where><if test=\"name != null\">and name like concat('%',#{namePattern},'%')</if><if test=\"name == null\">and 1 = -1</if></where>";

        Map<String,Object> map = new HashMap<>();
        map.put("name","小");
        List<Map<String,Object>> result = this.dynamicExcuterService.selectByProvider(sql, map);
        System.out.println(result);
    }

    @Test
    public void staticSqlTest() {
        String sql = "select * from students";
        List<Map> result = this.dynamicExcuterService.excute(sql, null);
        System.out.println(result);
    }

    @Test
    public void staticSqlTestBySqlProvider() {
        String sql = "select * from students";
        List<Map<String,Object>> result = this.dynamicExcuterService.selectByProvider(sql, null);
        System.out.println(result);
    }

    @Test
    public void includeSqlTest() {
        String sql = "select\n<include refid=\"com.example.template.mapper.DemoMapper.selectColumns\">\n<property name=\"alias\" value=\"t1\"/>\n</include>\nfrom students t1\nwhere id = #{id}";

        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        List<Map> result = this.dynamicExcuterService.excute(sql, map);
        System.out.println(result);
    }

    @Test
    public void includeSqlTestBySqlProvider() {
        String sql = "select\n<include refid=\"com.example.template.mapper.DemoMapper.selectColumns\">\n<property name=\"alias\" value=\"t1\"/>\n</include>\nfrom students t1\nwhere id = #{id}";

        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        List<Map<String,Object>> result = this.dynamicExcuterService.selectByProvider(sql, map);
        System.out.println(result);
    }
}
