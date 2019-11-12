# Swagger2

## 1.配置Swagger2

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