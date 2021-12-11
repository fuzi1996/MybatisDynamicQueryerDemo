package com.example.template.mapper;

import com.github.mybatisdq.DynamicSelectSqlProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

public interface DemoMapper {
    @SelectProvider(value = DynamicSelectSqlProvider.class,method = "getDynamicSelectSql")
    List<Map<String,Object>> dynamicQueryer(Map<String,Object> param);
}
