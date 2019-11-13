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