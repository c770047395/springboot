# Dubbo+Zookeeper

## Zookeeper的安装
### windows下的安装

1. 下载

网站：https://mirrors.tuna.tsinghua.edu.cn/apache/zookeeper/zookeeper-3.4.14/

2.配置

下载了之后解压，将conf下的zoo_simple.cfg复制一份，然后在bin里启动zkServer.cmd即可

### linux下的安装

1. 下载

2. 解压到/usr/local

3. 重命名为zookeeper

4. 在zookeeper下新建data与log文件夹

5. 修改配置文件
```properties
tickTime=2000

initLimit=10

syncLimit=5

dataDir=/usr/local/zookeeper/data
dataLogDir=/usr/local/zookeeper/logs

clientPort=2181

server.1=zookeeper:2888:38888
```

6. 配置etc/profile
```properties
# zookeeper
export ZOOKEEPER_HOME=/usr/local/zookeeper
export PATH=$ZOOKEEPER_HOME/bin:$PATH
export PATH
```

7. 命令
```shell script
#开启
zkServer.sh start
#查看状态
zkServer.sh status
#关闭
zkServer.sh stop
```

## dubbo和zookeeper的使用
1. 首先引入maven依赖
```xml
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-framework</artifactId>
    <version>2.12.0</version>
</dependency>
<dependency>
    <groupId>org.apache.curator</groupId>
    <artifactId>curator-recipes</artifactId>
    <version>2.12.0</version>
</dependency>
<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.14</version>
    <!--排除这个slf4j-log4j12-->
    <exclusions>
        <exclusion>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>com.github.sgroschupf</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.1</version>
</dependency>

<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.3</version>
</dependency>
```

2. 生产者配置dubbo
```properties
#服务应用的名字
dubbo.application.name=provider_server
#注册中心的地址
dubbo.registry.address=zookeeper://127.0.0.1:2181
#那些服务要被注册
dubbo.scan.base-packages=com.cp.service
```

3. 消费者配置
```properties
#消费者去哪里拿服务需要暴露自己的名字
dubbo.application.name=consumer_server
#注册中心的地址
dubbo.registry.address=zookeeper://127.0.0.1:2181
```

4. 生产者提供服务:在提供服务的类上加上@Service注解（注意不是spring的Service是dubbo的），所以使用@Component交给spring管理
```java
@Service//dubbo注册服务，不要与spring的弄混了
@Component//使用了dubbo后尽量不要使用service
public class TicketServiceImpl implements TicketService{
    @Override
    public String getTicket() {
        return "cpcpcpwmwmwm";
    }
}
```

5. 消费者使用服务
   - 方法1：在服务提供商同样目录下新建同样接口,然后在需要使用的地方使用@Reference引用即可（一般不使用）
   ```java
    @Service
    public class UserService {
        //想拿到票,要去注册中心拿到服务
        @Reference//引用
        TicketService ticketService;
    
        public void buyTicket(){
            String ticket = ticketService.getTicket();
            System.out.println("在注册中心拿到票了："+ticket);
        }
    
    }
   ```