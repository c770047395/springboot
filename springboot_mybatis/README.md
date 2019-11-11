# SpringBoot整合Mybatis
1. 首先，还是要导入依赖
```xml
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>2.1.1</version>
</dependency>
```
2. 然后编写实体类和Mapper接口
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;
    private String pwd;
}
```
@Mapper让Spring识别这是一个Mapper，在编译时会生成对应的实现类，也可以在主入口使用@MapperScan代替
```java
@Mapper//也可以在Application中使用MapperScan注解扫描
@Repository
public interface UserMapper {
    List<User> queryUserList();
    User queryUserById(int id);
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(int id);
}
```
3. 编写对应的Mapper.xml文件
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.cp.springboot_mybatis.mapper.UserMapper">

    <select id="queryUserList" resultType="User">
        select * from user
    </select>
    <select id="queryUserById" resultType="User">
    select * from user where id = #{id}
</select>

    <insert id="addUser" parameterType="User">
    insert into user (id,name,pwd) values (#{id},#{name},#{pwd})
</insert>

    <update id="updateUser" parameterType="User">
    update user set name=#{name},pwd=#{pwd} where id = #{id}
</update>

    <delete id="deleteUser" parameterType="int">
    delete from user where id = #{id}
</delete>
</mapper>
```
4. 在全局配置文件中配置别名以及xml路径
```properties
#整合mybatis
mybatis.type-aliases-package=com.cp.springboot_mybatis.pojo
mybatis.mapper-locations=classpath:mybatis/mapper/*.xml
```
经过以上四步就完成了Springboot对Mybatis的整合