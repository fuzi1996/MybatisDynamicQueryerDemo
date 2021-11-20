package com.example.template.service;

import java.util.List;
import java.util.Map;

public interface DynamicExcuterService {

    List<Map> excute(String sqlScript, Object param);

}
