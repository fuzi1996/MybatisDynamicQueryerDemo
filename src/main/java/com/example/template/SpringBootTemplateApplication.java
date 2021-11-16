package com.example.template;

import com.fuzi1996.demo.MyBatisDynamicQueryer;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringBootTemplateApplication {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTemplateApplication.class, args);
    }

    @Bean
    public MyBatisDynamicQueryer getMyBatisDynamicQueryer(){
        return new MyBatisDynamicQueryer(sqlSessionFactory);
    }

}
