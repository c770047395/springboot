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

# 使用Springboot发送邮件
1. 引入依赖
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

2. 配置Mail信息
```yaml
spring.mail.username=770047395@qq.com
spring.mail.password=mmwbtzodxqcxbeac
spring.mail.host=smtp.qq.com
# 开启加密验证
spring.mail.properties.mail.smtp.ssl.enable=true
```

3. 测试
```java
@SpringBootTest
class SpringbootTestApplicationTests {

    @Autowired
    JavaMailSenderImpl mailSender;

    @Test
    void contextLoads() {
        //一个简单的邮件
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject("你好！");
        simpleMailMessage.setText("谢谢你");
        simpleMailMessage.setTo("770047395@qq.com");
        simpleMailMessage.setFrom("770047395@qq.com");
        mailSender.send(simpleMailMessage);
    }

    @Test
    void contextLoads2() throws MessagingException {
        //一个复杂的邮件
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);


        helper.setSubject("hhh");
        helper.setText("<p style='color:red'>asdlkjlasdj</p>",true);

        helper.setFrom("770047395@qq.com");
        helper.setTo("770047395@qq.com");

        mailSender.send(mimeMessage);
    }

}
```