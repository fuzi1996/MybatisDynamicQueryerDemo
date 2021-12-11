package com.example.template;

import com.github.mybatisdq.MyBatisDynamicQueryer;
import com.github.mybatisdq.cache.GlobalConfigurationCache;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
@MapperScan(basePackages = "com.example.template.mapper")
@SpringBootApplication
public class SpringBootTemplateApplication {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootTemplateApplication.class, args);
        SqlSessionFactory sqlSessionFactory = context.getBean(SqlSessionFactory.class);
        GlobalConfigurationCache.setConfiguration(sqlSessionFactory.getConfiguration());
    }

    @Bean
    public MyBatisDynamicQueryer getMyBatisDynamicQueryer(){
        return new MyBatisDynamicQueryer(sqlSessionFactory);
    }

}
