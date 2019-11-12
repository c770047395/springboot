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
配置Swagger需要注册一个Docket的bean到配置中,通过阅读源码发现需要添加一个DocumentationType类，这里使用自带的

然后通过Docket.apiInfo()方法可以配置页面的一些静态信息，需要传入一个ApiInfo参数。
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