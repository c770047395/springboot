# Swagger2

## 1.集成Swagger2

1. 引入maven依赖

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
```
2. 新建配置文件SwaggerConfig
```java
@Configuration
@EnableSwagger2 //开启Swagger2
public class SwaggerConfig {
}
```
3. 访问localhost:8080/swagger-ui.html即可进入swagger页面

## 2.配置Swagger

配置Swagger需要注册一个Docket的bean到配置中,通过阅读源码发现需要添加一个DocumentationType类，这里使用自带的SWAGGER_2

### 2.1配置基础信息

通过Docket.apiInfo()方法可以配置页面的一些静态信息，需要传入一个ApiInfo参数。
```java
@Configuration
@EnableSwagger2 //开启Swagger2
public class SwaggerConfig {

    //配置了Swagger的Docket的bean实例
    @Bean
    public Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo());
    }

    //配置Swagger信息=apiInfo
    private ApiInfo apiInfo(){
        return new ApiInfo("CP的SwaggerAPI文档",
                "嘻嘻嘻",
                "1.0",
                "47.96.128.98",
                //作者信息
                new Contact("cp","47.96.128.98","770047395@qq.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());

    }
}

```

### 2.2配置扫描接口以及开关
这些配置也同样在Docket里配置

.select()与.build()是一对方法对，中间只能加apis或者paths方法，apis是指定要扫描的包等等，paths是指定只扫描哪些

enable(Boolean)方法是Swagger2的开关，这里我们可以使用Environment 获取外部配置文件
，在不同的环境选择开/关Swagger2
```java
//配置了Swagger的Docket的bean实例
@Bean
public Docket docket(Environment environment){
    //设置显示的swagger环境
    Profiles profiles = Profiles.of("test","dev");
    //通过environment.acceptsProfiles判断是否处在自己设定的环境中
    boolean flag = environment.acceptsProfiles(profiles);
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            //enable是否启用Swagger，如果为false，则不能在浏览器中访问
            .enable(flag)
            .select()
            //RequestHandlerSelectors,配置扫描接口的方式
            //basePackage()指定要扫描的包
            //any()扫描全部
            //none()都不扫描
            //withClassAnnotation()扫描类上的注解
            //withMethodAnnotation()扫描方法上的注解
            .apis(RequestHandlerSelectors.basePackage("com.cp.swagger_demo.controller"))
            //paths 过滤，只扫描
            .paths(PathSelectors.ant("/**"))
            .build();
}
```