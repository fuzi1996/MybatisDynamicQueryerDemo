package com.example.template.service.impl;

import com.example.template.mapper.DemoMapper;
import com.example.template.service.DynamicExcuterService;
import com.github.mybatisdq.cache.GlobalConfigurationCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringBootTest
public class MultiThreadQueryTest {

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private DynamicExcuterService dynamicExcuterService;

    @Test
    public void multiThreadTest(){
        int MAX_COUNT = 10000;
        CountDownLatch countDownLatch = new CountDownLatch(MAX_COUNT);
        ArrayList<Thread> threads = new ArrayList<>(MAX_COUNT);
        Random random = new Random();
        AtomicInteger currentCount = new AtomicInteger();
        AtomicInteger unCurrentCount = new AtomicInteger();
        ArrayList<String> unCurrentInfo = new ArrayList<>();
        for(int i=0;i<MAX_COUNT;i++){
            threads.add(new MultiThreadQueryer(countDownLatch,dynamicExcuterService,random,currentCount,unCurrentCount,unCurrentInfo));
        }
        threads.stream().forEach(t->{
            t.start();
        });
        try {
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        unCurrentInfo.stream().forEach(System.out::println);
        System.out.println(String.format("currentCount:%s,uncurrentCount:%s,total:%s",currentCount.get(),unCurrentCount.get(),currentCount.get()+unCurrentCount.get()));
        Assertions.assertEquals(MAX_COUNT,currentCount.get());
    }

    @Test
    public void multiThreadTestBySqlProvider(){
        GlobalConfigurationCache.setConfiguration(sqlSessionFactory.getConfiguration());

        int MAX_COUNT = 10000;
        CountDownLatch countDownLatch = new CountDownLatch(MAX_COUNT);
        ArrayList<Thread> threads = new ArrayList<>(MAX_COUNT);
        Random random = new Random();
        AtomicInteger currentCount = new AtomicInteger();
        AtomicInteger unCurrentCount = new AtomicInteger();
        ArrayList<String> unCurrentInfo = new ArrayList<>();
        for(int i=0;i<MAX_COUNT;i++){
            threads.add(new MultiThreadQueryerBySqlProvider(countDownLatch,dynamicExcuterService,random,currentCount,unCurrentCount,unCurrentInfo));
        }
        threads.stream().forEach(t->{
            t.start();
        });
        try {
            countDownLatch.await();
        }catch (Exception e){
            e.printStackTrace();
        }
        unCurrentInfo.stream().forEach(System.out::println);
        System.out.println(String.format("currentCount:%s,uncurrentCount:%s,total:%s",currentCount.get(),unCurrentCount.get(),currentCount.get()+unCurrentCount.get()));
        Assertions.assertEquals(MAX_COUNT,currentCount.get());
    }
}

@Slf4j
class MultiThreadQueryer extends Thread{

    private CountDownLatch countDownLatch;
    private DynamicExcuterService dynamicExcuterService;
    private Random random;
    private AtomicInteger currentCount;
    private AtomicInteger unCurrentCount;
    private ArrayList<String> unCurrentInfo;

    public MultiThreadQueryer(CountDownLatch countDownLatch,DynamicExcuterService dynamicExcuterService,Random random,AtomicInteger currentCount,AtomicInteger unCurrentCount,ArrayList<String> unCurrentInfo){
        this.countDownLatch = countDownLatch;
        this.dynamicExcuterService = dynamicExcuterService;
        this.random = random;
        this.currentCount = currentCount;
        this.unCurrentCount = unCurrentCount;
        this.unCurrentInfo = unCurrentInfo;
    }

    @Override
    public void run() {
        String sql = "select\n<include refid=\"com.example.template.mapper.DemoMapper.selectColumns\">\n<property name=\"alias\" value=\"t1\"/>\n</include>\nfrom students t1\nwhere id = #{id}";
        Map<String,Object> map = new HashMap<>();
        int id = (random.nextInt(11)+1);
        map.put("id",id);
        List<Map> result = new ArrayList<>();
        try {
            result = this.dynamicExcuterService.excute(sql, map);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            countDownLatch.countDown();
            if(null != result && !result.isEmpty()){
                long resultId = (long)result.get(0).get("id");
                if(id != resultId){
                    unCurrentInfo.add(String.format("id:%s,result id:%s",id,resultId));
                    unCurrentCount.incrementAndGet();
                }else{
                    currentCount.incrementAndGet();
                }
            }else{
                unCurrentCount.incrementAndGet();
                if(null != result){
                    unCurrentInfo.add(String.format("id:%s,result isEmpty,count:%s",id,countDownLatch.getCount()));
                }else{
                    unCurrentInfo.add(String.format("id:%s,result is null,count:%s",id,countDownLatch.getCount()));
                }
            }
        }
    }
}

@Slf4j
class MultiThreadQueryerBySqlProvider extends Thread{

    private CountDownLatch countDownLatch;
    private DynamicExcuterService dynamicExcuterService;
    private Random random;
    private AtomicInteger currentCount;
    private AtomicInteger unCurrentCount;
    private ArrayList<String> unCurrentInfo;

    public MultiThreadQueryerBySqlProvider(CountDownLatch countDownLatch,DynamicExcuterService dynamicExcuterService,Random random,AtomicInteger currentCount,AtomicInteger unCurrentCount,ArrayList<String> unCurrentInfo){
        this.countDownLatch = countDownLatch;
        this.dynamicExcuterService = dynamicExcuterService;
        this.random = random;
        this.currentCount = currentCount;
        this.unCurrentCount = unCurrentCount;
        this.unCurrentInfo = unCurrentInfo;
    }

    @Override
    public void run() {
        String sql = "select\n<include refid=\"com.example.template.mapper.DemoMapper.selectColumns\">\n<property name=\"alias\" value=\"t1\"/>\n</include>\nfrom students t1\nwhere id = #{id}";
        Map<String,Object> map = new HashMap<>();
        int id = (random.nextInt(11)+1);
        map.put("id",id);
        List<Map<String,Object>> result = new ArrayList<>();
        try {
            result = dynamicExcuterService.selectByProvider(sql, map);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            countDownLatch.countDown();
            if(null != result && !result.isEmpty()){
                long resultId = (long)result.get(0).get("id");
                if(id != resultId){
                    unCurrentInfo.add(String.format("id:%s,result id:%s",id,resultId));
                    unCurrentCount.incrementAndGet();
                }else{
                    currentCount.incrementAndGet();
                }
            }else{
                unCurrentCount.incrementAndGet();
                if(null != result){
                    unCurrentInfo.add(String.format("id:%s,result isEmpty,count:%s",id,countDownLatch.getCount()));
                }else{
                    unCurrentInfo.add(String.format("id:%s,result is null,count:%s",id,countDownLatch.getCount()));
                }
            }
        }
    }
}
