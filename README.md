# SpringBoot

## spring回顾
### 什么是spring
Spring是一个开源框架，2003 年兴起的一个轻量级的Java 开发框架，作者：Rod Johnson 。
Spring是为了解决企业级应用开发的复杂性而创建的，简化开发。
### spring是如何简化java开发的
为了降低Java开发的复杂性，Spring采用了以下4种关键策略：
1. 基于POJO的轻量级和最小侵入性编程；
2. 通过IOC，依赖注入（DI）和面向接口实现松耦合；
3. 基于切面（AOP）和惯例进行声明式编程；
4. 通过切面和模版减少样式代码；

## springboot初体验
### springboot的优点
1. 为所有Spring开发者更快的入门
2. 开箱即用，提供各种默认配置来简化项目配置
3. 内嵌式容器简化Web项目
4. 没有冗余代码生成和XML配置的要求

## SpringBoot运行原理
springboot的核心思想：**自动配置**
原理
```
starter就是一个个场景启动器
假如要配置web项目，只需要导入web项目的启动器就可以了，springboot就会帮我们自动配置web所需要的一切
```

如何实现自动配置（简约版）：

Spring Boot启动的时候会通过@EnableAutoConfiguration
注解找到META-INF/spring.factories配置文件中的所有自动
配置类，并对其进行加载，而这些自动配置类都是以AutoConfiguration结尾来命名的，
它实际上就是一个JavaConfig形式的Spring容器配置类，它能通过以Properties结尾命
名的类中取得在全局配置文件中配置的属性如：server.port，而XxxxProperties类是
通过@ConfigurationProperties注解与全局配置文件中对应的属性进行绑定的。
XxxxProperties类中的属性可以暴露给用户进行主动配置，如果不进行主动配置则会使用默认配置

在启动文件中有个@SpringBootApplication注解，@SpringBootApplication中有三个比较重要的注解，一个是@SpringBootConfiguration
另一个是@EnableAutoConfiguration，还有一个是@ComponentScan

- @SpringBootConfiguration实际上就是一个@Configuration注解，只是特殊表明了这是一个SpringBoot的配置文件
- @EnableAutoConfiguration是自动配置和核心，其中有两个重要注解
   1. @AutoConfigurationPackage
   2. @Import(AutoConfigurationImportSelector.class)：借助AutoConfigurationImportSelector读取
   META-INF/spring.factories配置文件，将符合条件的包都加载到IoC容器
- @ComponentScan相当于spring配置文件中的``<context:component-scan>``，会自动扫描该配置类所在的包下的所有使用注解的类并自动交给spring容器管理

在META-INF/spring.factories配置文件中的每个类都需要配置以下注解：

- @ConditionalOnxxxx：条件注解，用于判断类是否需要加载
- @EnableConfigurationProperties(XxxxProperties.class)：使使用@ConfigurationProperties注解的类生效
相当于把@ConfigurationProperties标注的类进行交给Spring容器管理，而@ConfigurationProperties注解通过填写prefix属性将类中可配置部分
暴露给配置文件，使得在配置文件中的属性与组件中一一对应。如果配置文件中没配置，则使用默认的配置。

### 详解配置文件

SpringBoot使用一个全局的配置文件 ， 配置文件名称是固定的

application.properties
语法结构 ： key=value
application.yml
语法结构 ：key：空格 value
配置文件的作用 ：修改SpringBoot自动配置的默认值，因为SpringBoot在底层都给我们自动配置好了；

配置文件是如何注入组件的呢？
原理是通过@ConfigurationProperties注解，使配置文件中可以通过指定前缀访问这个类的属性。
```
例如我们使用@ConfigurationProperties(prefix = "person")
则在配置文件中可以通过person.xxx来访问这个类的属性
```

1. 首先导入配置文件处理器
```xml
<!--导入配置文件处理器，配置文件进行绑定就会有提示-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

2. 编写yml配置文件
```yaml
person:
  name: cp
  age: 5
  happy: false
  birth: 1998/1/11
  maps: {h1: hello1,h2: hello2}
  lists: [cp1,cp2,cp3]
  dog:
    name: dog
    age: 1
```

3. 在SpringBoot的主程序的同级目录下建包，只有这样，主程序才会对这些类生效；我们建一个pojo的包放入我们的Person类和Dog类
```java
package com.cp.springboot_demo02.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/*
@ConfigurationProperties作用：
将配置文件中配置的每一个属性的值，映射到这个组件中；
告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定
参数 prefix = “person” : 将配置文件中的person下面的所有属性一一对应

只有这个组件是容器中的组件，才能使用容器提供的@ConfigurationProperties功能
*/
@Component//被容器托管
@ConfigurationProperties(prefix = "person")
public class Person {
    private String name;
    private Integer age;
    private Boolean happy;
    private Date birth;
    private Map<String,Object> maps;
    private List<Object> lists;
    private Dog dog;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getHappy() {
        return happy;
    }

    public void setHappy(Boolean happy) {
        this.happy = happy;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public Map<String, Object> getMaps() {
        return maps;
    }

    public void setMaps(Map<String, Object> maps) {
        this.maps = maps;
    }

    public List<Object> getLists() {
        return lists;
    }

    public void setLists(List<Object> lists) {
        this.lists = lists;
    }

    public Dog getDog() {
        return dog;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", happy=" + happy +
                ", birth=" + birth +
                ", maps=" + maps +
                ", lists=" + lists +
                ", dog=" + dog +
                '}';
    }
}

```

```java
package com.cp.springboot_demo02.pojo;

import org.springframework.stereotype.Component;

@Component
public class Dog {
    private String name;
    private Integer age;

    public Dog(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public Dog() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

4. 在单元测试中进行测试
```java
package com.cp.springboot_demo02;

import com.cp.springboot_demo02.pojo.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootDemo02ApplicationTests {
    @Autowired
    Person person;
    @Test
    void contextLoads() {
        System.out.println(person);
    }
}
```

除了使用@ConfigurationProperties注解进行值的注入以外，还可以@Value注解进行注入

```java
@Component //注册bean
public class Person {
    //直接使用@value
    @Value("${person.name}") //从配置文件中取值
    private String name;
    @Value("#{11*2}")  //#{SPEL} Spring表达式
    private Integer age;
    @Value("true")  // 字面量
    private Boolean happy;
    
    。。。。。。  
}
```
这个由于要为每个属性单独注解，使用起来并不友好，并且功能不如@ConfigurationProperties强大
![](https://img2018.cnblogs.com/blog/1418974/201907/1418974-20190728164637719-1038154879.png)

- 松散绑定：对于配置文件与类中的值不需要完全相同，比如配置文件中是last-name类是lastName也能绑定上
- JSR303数据校验：可以对传入的值进行校验，通过@Validate和@Xxxx注解（@Xxxx为具体的校验规则如@Email）
 
结论：

- 配置yml和配置properties都可以获取到值 ， 强烈推荐 yml
- 如果我们在某个业务中，只需要获取配置文件中的某个值，可以使用一下 @value
- 如果说，我们专门编写了一个JavaBean来和配置文件进行映射，就直接使用@configurationProperties，不要犹豫！