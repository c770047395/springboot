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