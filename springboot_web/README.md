## 静态资源可以存放的位置：
```
1. classpath:/public/**
2. classpath:/resources/**
3. classpath:/static/**
4. classpath:/META-INF/resources/**
```
通过WebMvcAutoConfiguration的源码分析，发现其中``addResourceHandlers()``方法设置了三种静态资源路径方式
```java
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //1. 自己设置（一般不使用用）
    if (!this.resourceProperties.isAddMappings()) {
        logger.debug("Default resource handling disabled");
    } else {
        Duration cachePeriod = this.resourceProperties.getCache().getPeriod();
        CacheControl cacheControl = this.resourceProperties.getCache().getCachecontrol().toHttpCacheControl();
        //2. 通过导入jar包的方式，静态资源的jar包要符合命名规范lasspath:/META-INF/resources/webjars/，并且访问时通过/webjars/**访问
        if (!registry.hasMappingForPattern("/webjars/**")) {
            this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"}).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
        }
        //3. 通过直接导入静态文件的方式，在this.resourceProperties.getStaticLocations()中返回了四个路径：
        // "classpath:/META-INF/resources/", "classpath:/resources/", "classpath:/static/", "classpath:/public/"
        // 并且partten为/**，所以直接使用localhost:port/想要的资源，就可以获取静态文件
        String staticPathPattern = this.mvcProperties.getStaticPathPattern();
        if (!registry.hasMappingForPattern(staticPathPattern)) {
            this.customizeResourceHandlerRegistration(registry.addResourceHandler(new String[]{staticPathPattern}).addResourceLocations(WebMvcAutoConfiguration.getResourceLocations(this.resourceProperties.getStaticLocations())).setCachePeriod(this.getSeconds(cachePeriod)).setCacheControl(cacheControl));
        }

    }
}
```

## Thymeleaf模板引擎

### Thymeleaf的使用
1. 导入Thymeleaf依赖
2. 编写controller
3. 编写html文件，需要放在templates下以html结尾

这些在ThymeleafProperties中可以看到
```java
@ConfigurationProperties(
    prefix = "spring.thymeleaf"
)
public class ThymeleafProperties {
    private static final Charset DEFAULT_ENCODING;
    public static final String DEFAULT_PREFIX = "classpath:/templates/";
    public static final String DEFAULT_SUFFIX = ".html";
    ...
}
```

### Thymeleaf语法

1. 导入命名空间
2. 判断选择使用哪种表达式
   - ${...}变量
   - *{...}变量
   - \#{...}对象
   - @{...}url
   - ~{...}引入Fragment
3. 要实现页面可以ctrl+F9刷新，需要关闭缓存``spring.thymeleaf.cache=false``
