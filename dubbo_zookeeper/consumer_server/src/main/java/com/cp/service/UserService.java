package com.cp.service;

import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

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
