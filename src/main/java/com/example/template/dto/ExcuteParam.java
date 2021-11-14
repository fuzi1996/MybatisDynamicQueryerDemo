package com.example.template.dto;

import java.util.Map;

public class ExcuteParam {
    private String sql;
    private Map<String,Object> param;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}
