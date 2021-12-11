# MybatisDynamicQueryerDemo

## 说明
MybatisDynamicQueryerDemo 作为 [MyBatisDynamicQueryer](https://github.com/fuzi1996/MyBatisDynamicQueryer) 的示例程序

## 使用

## MyBatisDynamicQueryer

### 执行流程
1. 根据动态脚本,参数类型,返回值类型,脚本命令类型生成缓存Key
2. 判断configuration的mappedStatements中是否含有该key
    - 若含有,直接返回该缓存key
    - 若不含有
      languageDriver创建SqlSource并生成缓存key对应的mappedStatement
3. 根据sqlSessionFactory创建sqlSession,最后查询得到返回值

### 初始化

```java
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Bean
    public MyBatisDynamicQueryer getMyBatisDynamicQueryer(){
        return new MyBatisDynamicQueryer(sqlSessionFactory);
    }
```

### 执行动态SQL字符串
```java
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
```

## DynamicSelectSqlProvider

### 初始化Configuration
```java
GlobalConfigurationCache.setConfiguration(sqlSessionFactory.getConfiguration());
```

### 新建Mapper
```java
@SelectProvider(value = DynamicSelectSqlProvider.class,method = "getDynamicSelectSql")
List<Map<String,Object>> dynamicQueryer(Map<String,Object> param);
```

### 使用

```java
if(null == param){
    param = new HashMap<>();
}
param.put(DynamicSelectConstant.getDefaultSqlValueKey(),sqlScript);
return this.demoMapper.dynamicQueryer(param);
```