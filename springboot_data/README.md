# SpringBoot连接数据库

## Springboot使用jdbc
springboot要使用jdbc需要引入两个依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <scope>runtime</scope>
</dependency>
```
所有可以配置的东西一样也可以在全局配置文件中配置

1. 首先要配置一个数据源(由于springboot默认导入高版本的连接器，所以url要配置一个时区，否则报错)：
```yaml
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/mybatis?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    driver-class-name: com.mysql.jdbc.Driver
```
2. 然后我们就可以使用数据源对数据库进行操作了
```java
@SpringBootTest
class SpringbootDataApplicationTests {

    @Autowired
    DataSource dataSource;

    @Test
    void contextLoads() throws SQLException {
        //查看一下默认的数据源
        System.out.println(dataSource.getClass());
        //获取数据库连接
        Connection connection = dataSource.getConnection();

        System.out.println(connection);
        connection.close();
    }

}
```
值得一提的是，SpringBoot中有许多Template类，这些类已经封装好了一些方法，可以直接调用，所以我们在使用jdbc时不用再写一大堆冗长的代码，可以直接使用Template类中的方法代替

```java
@RestController
public class JDBCController {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //查询数据库的所有信息
    @GetMapping("/userList")
    public List<Map<String,Object>> userList(){
        String sql = "select * from user";
        List<Map<String, Object>> list_maps = jdbcTemplate.queryForList(sql);
        return list_maps;
    }

    @GetMapping("addUser")
    public String addUser(){
        String sql = "insert into user(name,pwd) values('小敏','123456')";
        jdbcTemplate.update(sql);
        return "add-ok";
    }

    @GetMapping("updateUser/{id}")
    public String update(@PathVariable int id){
        String sql = "update user set name=?,pwd=? where id="+id;
        Object[] objects = new Object[2];
        objects[0]="小明2";
        objects[1]="222222";
        jdbcTemplate.update(sql,objects);
        return "update-ok";
    }

    @GetMapping("deleteUser/{id}")
    public String delete(@PathVariable int id) {
        String sql = "delete from user where id = "+id;
        jdbcTemplate.update(sql);
        return "delete-ok";
    }
}
```

## SpringBoot使用Druid数据源
1. 首先要引入maven依赖
```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>druid</artifactId>
    <version>1.1.21</version>
</dependency>
```
2. 在数据源中配置type
```yaml
spring:
  datasource:
    username: root
    password: 123456
    url: jdbc:mysql://localhost:3306/mybatis?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
    driver-class-name: com.mysql.jdbc.Driver
    #使用指定的数据源
    type: com.alibaba.druid.pool.DruidDataSource
```
使用了druid数据源之后可以增加一些额外的配置
```yaml
    #Spring Boot 默认是不注入这些属性值的，需要自己绑定
    #druid 数据源专有配置
    initialSize: 5
    minIdle: 5
    maxActive: 20
    maxWait: 60000
    timeBetweenEvictionRunsMillis: 60000
    minEvictableIdleTimeMillis: 300000
    validationQuery: SELECT 1 FROM DUAL
    testWhileIdle: true
    testOnBorrow: false
    testOnReturn: false
    poolPreparedStatements: true

    #配置监控统计拦截的filters，stat:监控统计、log4j：日志记录、wall：防御sql注入
    #如果允许时报错  java.lang.ClassNotFoundException: org.apache.log4j.Priority
    #则导入 log4j 依赖即可，Maven 地址： https://mvnrepository.com/artifact/log4j/log4j
    filters: stat,wall,log4j
    maxPoolPreparedStatementPerConnectionSize: 20
    useGlobalDataSourceStat: true
    connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
```
3. druid强大的地方还在于可以自定义配置文件

首先要注入一个DruidDataSource对象，并将他暴露给配置文件
```java
@Configuration
public class DruidConfig {

    @ConfigurationProperties(prefix="spring.datasource")
    @Bean
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }
}
```
然后就可以对其进行拓展了，常用的有后台管理系统和流量统计的拓展
```java
//后台监控 : web.xml
//因为SpringBoot内置了servlet容器，所以没有web.xml，所以可以使用配置类代替
@Bean
public ServletRegistrationBean statViewServlet(){
    ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");

    //后台需要有人登陆

    HashMap<String, String> initParameters = new HashMap<>();
    //增加配置
    initParameters.put("loginUsername","admin");
    initParameters.put("loginPassword","123456");

    //允许谁可以访问
    initParameters.put("allow","");

    //禁止谁能访问
    //initParameters.put("cp","192.128.19.1");

    bean.setInitParameters(initParameters);//设置初始化参数
    return bean;
}
@Bean
public FilterRegistrationBean webStatFilter(){
    FilterRegistrationBean<Filter> bean = new FilterRegistrationBean<>();
    bean.setFilter(new WebStatFilter());

    Map<String, String> initParameters = new HashMap<>();
    //可以过滤那些请求呢
    bean.setInitParameters(initParameters);

    initParameters.put("exclusions","js,*.css,/druid/*");
    bean.setInitParameters(initParameters);
    return bean;
}
```