package com.cp.service;


import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Service//dubbo注册服务，不要与spring的弄混了
@Component//使用了dubbo后尽量不要使用service
public class TicketServiceImpl implements TicketService{
    @Override
    public String getTicket() {
        return "cpcpcpwmwmwm";
    }
}
