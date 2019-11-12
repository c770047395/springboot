package com.cp.springboot_test.service;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledService {

    //在特定时间执行这个方法
    //cron表达式
    //秒 分 时 日 月 周几
    @Scheduled(cron="*/5 * * * * 0-7")
    public void hello(){
        System.out.println("你被执行了");
    }
}
