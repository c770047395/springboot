# 异步任务

如果有些不需要与主流程同步的任务，需要后台异步处理，但是不能让前端等待，这时异步任务就登场了

开启一个异步任务很简单

1. 在启动类上加上``@EnableAsync``注解

```java
//开启异步注解功能
@EnableAsync
@SpringBootApplication
public class SpringbootTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootTestApplication.class, args);
    }

}
```
2. 在异步方法上加上``@Async``注解
```java
@Service
public class AsyncService {

    //告诉Spring这个是一个异步方法
    @Async
    public void hello(){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("数据正在处理");
    }
}
```

然后Spring就会自动帮我们开启一个线程执行异步任务