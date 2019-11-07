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
相当于把@ConfigurationProperties标注的类进行一次注入，而@ConfigurationProperties注解通过填写prefix属性将类中可配置部分
暴露给配置文件，使得在配置文件中可以修改。如果配置文件中没配置，则使用默认的配置。